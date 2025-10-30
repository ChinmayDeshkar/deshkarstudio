package com.crm.deshkarStudio.services.impl;

import com.crm.deshkarStudio.dto.JwtResponse;
import com.crm.deshkarStudio.dto.LoginRequest;
import com.crm.deshkarStudio.dto.SignupRequest;
import com.crm.deshkarStudio.model.Role;
import com.crm.deshkarStudio.model.User;
import com.crm.deshkarStudio.repo.UserRepo;
import com.crm.deshkarStudio.services.AuthService;
import com.crm.deshkarStudio.services.JwtUtil;
import com.crm.deshkarStudio.services.OtpService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepo userRepo;
    private final OtpService otpService;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private final Map<String, SignupRequest> pendingSignup = new HashMap<>();

    // STEP 1: Request OTP for signup
    public ResponseEntity<?> requestSignupOtp(SignupRequest req) {
        System.out.println(req);
        if (userRepo.existsByPhone(req.phone())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Phone already registered"));
        }
        otpService.sendOtp(req.phone());
        pendingSignup.put(req.phone(), req);
        return ResponseEntity.ok(Map.of("message", "OTP sent successfully"));
    }

    // STEP 2: Verify OTP & register
    public ResponseEntity<?> verifySignupOtp(String phone, String otp) {
        if (!otpService.verifyOtp(phone, otp)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid OTP"));
        }

        SignupRequest req = pendingSignup.get(phone);
        if (req == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "No signup data found"));
        }

        User user = new User();
        user.setUsername(req.username());
        user.setPhone(req.phone());
        user.setEmail(req.email());
        user.setPassword(encoder.encode(req.password()));
        user.setRole(Role.ROLE_USER);

        userRepo.save(user);
        pendingSignup.remove(phone);

        return ResponseEntity.ok(Map.of("message", "Signup successful"));
    }

    // LOGIN OTP FLOW
    public ResponseEntity<?> requestLoginOtp(String phone) {
        if (!userRepo.existsByPhone(phone)) {
            return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
        }
        otpService.sendOtp(phone);
        return ResponseEntity.ok(Map.of("message", "OTP sent successfully"));
    }

    public ResponseEntity<?> verifyLoginOtp(String phone, String otp) {
        if (!otpService.verifyOtp(phone, otp)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid OTP"));
        }

        User user = userRepo.findByPhone(phone).orElseThrow();
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        return ResponseEntity.ok(Map.of(
                "message", "Login successful",
                "accessToken", token,
                "role", user.getRole().name()
        ));
    }
}
