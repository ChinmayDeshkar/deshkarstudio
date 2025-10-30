package com.crm.deshkarStudio.contoller;


import com.crm.deshkarStudio.dto.JwtResponse;
import com.crm.deshkarStudio.dto.LoginRequest;
import com.crm.deshkarStudio.dto.SignupRequest;
import com.crm.deshkarStudio.model.Role;
import com.crm.deshkarStudio.model.User;
import com.crm.deshkarStudio.repo.UserRepo;
import com.crm.deshkarStudio.services.AuthService;
import com.crm.deshkarStudio.services.JwtUtil;
import com.crm.deshkarStudio.services.OtpService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup-request")
    public ResponseEntity<?> signupRequest(@Valid @RequestBody SignupRequest req) {
        System.out.println(req);
        // Step 1: Temporarily store signup data & send OTP
        return authService.requestSignupOtp(req);
    }

    @PostMapping("/signup-verify")
    public ResponseEntity<?> signupVerify(@RequestBody Map<String, String> body) {
        // Step 2: Verify OTP and create user
        return authService.verifySignupOtp(body.get("phoneNumber"), body.get("otp"));
    }

    @PostMapping("/login-request")
    public ResponseEntity<?> loginRequest(@RequestBody Map<String, String> body) {
        return authService.requestLoginOtp(body.get("phoneNumber"));
    }

    @PostMapping("/login-verify")
    public ResponseEntity<?> loginVerify(@RequestBody Map<String, String> body) {
        return authService.verifyLoginOtp(body.get("phoneNumber"), body.get("otp"));
    }
}