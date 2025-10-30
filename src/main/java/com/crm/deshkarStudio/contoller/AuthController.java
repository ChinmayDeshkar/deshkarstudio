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
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest req) {

        return authService.signUp(req);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        return authService.login(req);
    }

    @PostMapping("/request-otp")
    public ResponseEntity<Map <String, String>> requestOtp(@RequestBody Map<String, String> body) {

        return authService.requestOtp(body.get("phoneNumber"));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, String>> verifyOtp(@RequestBody Map<String, String> body) {
        return authService.verifyOtp(body.get("phoneNumber"), body.get("otp"));
    }
}