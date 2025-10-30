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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authManager;
    private final UserRepo userRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final OtpService otpService;

    @Override
    public ResponseEntity<?> signUp(SignupRequest req) {

        // check unique constraints
        if (userRepo.existsByUsername(req.username())) return ResponseEntity.badRequest().body("Username already taken");
        if (userRepo.existsByEmail(req.email())) return ResponseEntity.badRequest().body("Email already taken");
        if (userRepo.existsByPhone(req.phone())) return ResponseEntity.badRequest().body("Phone already taken");

        var user = new User();
        user.setUsername(req.username());
        user.setPassword(passwordEncoder.encode(req.password()));
        user.setEmail(req.email());
        user.setPhone(req.phone());
        // map role string to Role enum
        Role role;
        try {
            role = switch(req.role().toUpperCase()) {
                case "ADMIN" -> Role.ROLE_ADMIN;
                case "EMPLOYEE" -> Role.ROLE_EMPLOYEE;
                default -> Role.ROLE_USER;
            };
        } catch (Exception e) {
            role = Role.ROLE_USER;
        }
        user.setRole(role);

        userRepo.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created");
    }

    @Override
    public ResponseEntity<?> login(LoginRequest req) {
        try {
            var authToken = new UsernamePasswordAuthenticationToken(req.username(), req.password());
            authManager.authenticate(authToken);
            // If no exception, user is authenticated — load user to get role
            var user = userRepo.findByUsername(req.username()).orElseThrow();
            String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name().replace("ROLE_",""));
            return ResponseEntity.ok(new JwtResponse(token, "Bearer", user.getUsername(), user.getRole().name()));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @Override
    public ResponseEntity<Map<String, String>> requestOtp(String phoneNumber) {
        otpService.sendOtp(phoneNumber);
        return ResponseEntity.ok(Map.of("message", "OTP sent successfully"));
    }

    @Override
    public ResponseEntity<Map<String, String>> verifyOtp(String phoneNumber, String otp) {

        boolean verified = otpService.verifyOtp(phoneNumber,otp);
        if (verified) {
            User user = userRepo.findByPhone(phoneNumber).orElseThrow(() -> new RuntimeException("Phone number not exists"));
            String token = jwtUtil.generateToken(phoneNumber, user.getRole().toString());
            return ResponseEntity.ok(Map.of("token", token));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid OTP"));
    }
}
