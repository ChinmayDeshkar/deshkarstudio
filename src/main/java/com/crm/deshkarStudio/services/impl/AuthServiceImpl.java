package com.crm.deshkarStudio.services.impl;

import com.crm.deshkarStudio.dto.JwtResponse;
import com.crm.deshkarStudio.dto.LoginRequest;
import com.crm.deshkarStudio.dto.ResetPassword;
import com.crm.deshkarStudio.dto.SignupRequest;
import com.crm.deshkarStudio.model.Role;
import com.crm.deshkarStudio.model.User;
import com.crm.deshkarStudio.repo.UserRepo;
import com.crm.deshkarStudio.security.PasswordGenerator;
import com.crm.deshkarStudio.services.AuthService;
import com.crm.deshkarStudio.services.EmailService;
import com.crm.deshkarStudio.services.JwtUtil;
import com.crm.deshkarStudio.services.OtpService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.parameters.P;
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
    private final PasswordGenerator passwordGenerator;
    private final EmailService emailService;

    @Override
    public ResponseEntity<?> login(LoginRequest req) {
        log.info("Login Request for: " + req);
        User user = userRepo.findByUsername(req.username())
                .orElse(null);

        if(user == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("Error", "UserNotFound",
                                                                                "Message", "User not found with username " + req.username()));

        if(!user.isActive())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("Error", "User is inactive",
                    "Message", "User is inactive " + req.username()));

        if(encoder.matches(req.password(), user.getPassword())){

            String authToken = jwtUtil.generateToken(req.username(), user.getRole().toString());
            if (user.isFirstLogin()) return ResponseEntity.status(HttpStatus.OK).body(Map.of("Message", "First Login detected, Please reset Password"));

            log.info(user.getRole().toString());

            String otp = otpService.sendOtp(user.getUsername());

            // Tell frontend to go to OTP verify page
            return ResponseEntity.ok(Map.of(
                    "otpSent", true,
                    "username", user.getUsername(),
                    "otp", otp
                    ));
//            return ResponseEntity.status(HttpStatus.OK).body(Map.of("Message", "User logged in successfully",
//                                                                        "AuthToken", authToken,
//                                                                        "Role", user.getRole(),
//                                                                        "Username", user.getUsername()));
        }


        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("Error", "Error while logging in"));
    }

    @Override
    public ResponseEntity<?> signup(User newUser) {
        // Check if username presents
        if(userRepo.existsByUsername(newUser.getUsername()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("Message: ", "User already present"));

        // Check if phone number present
        if(userRepo.existsByPhone(newUser.getPhone()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("Message: ", "Phone number already present"));

        if (userRepo.existsByEmail(newUser.getEmail()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("Message: ", "Email id already present"));

        String newPassword = passwordGenerator.generatePassword(8);
        User user = newUser;
        user.setRole(Role.ROLE_EMPLOYEE);
        user.setPassword(encoder.encode(newPassword));
        int userCount = userRepo.findAll().size();
        String userId = String.format("d%04d", userCount + 1);
        user.setUsername(userId);

        userRepo.save(user);
        emailService.createAndSendMailForNewUser("deshkarchinmay42@gmail.com", "New user created - " + newUser.getUsername(), newUser, newPassword);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("Message: ", "User Created",
                                                                        "Username: ", user.getUsername()));


    }

    @Override
    public ResponseEntity<?> firstLogin(ResetPassword req){
        var u = userRepo.findByUsername(req.getUsername());
        if(u.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("Message","User not found"));
        User user = u.get();
        if(!encoder.matches(req.getOldPassword(), user.getPassword()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("Message","Incorrect old password"));
        user.setPassword(encoder.encode(req.getNewPassword()));
        user.setFirstLogin(false);
        userRepo.save(user);
        return ResponseEntity.ok(Map.of("Message","Password reset successful"));
    }

    @Override
    public Boolean validateToken(String token) {
        boolean isValid = false;
        if(jwtUtil.validateToken(token))
            isValid = true;
        return isValid;
    }

    @Override
    public ResponseEntity<?> verifyOtp(Map<String, String> req) {
        String username = req.get("username");
        String otp = req.get("otp");

        if (!otpService.verifyOtp(username, otp)) {
            return ResponseEntity.badRequest().body(Map.of("Message", "Invalid OTP"));
        }

        User user = userRepo.findByUsername(username).orElse(null);
        if (user == null) return ResponseEntity.status(404).body(Map.of("Message", "User not found"));

        // Generate JWT (same as before)
        String authToken = jwtUtil.generateToken(username, user.getRole().toString());

        return ResponseEntity.ok(Map.of(
                "Message", "Login successful",
                "AuthToken", authToken,
                "Role", user.getRole(),
                "Username", username
        ));
    }

}
