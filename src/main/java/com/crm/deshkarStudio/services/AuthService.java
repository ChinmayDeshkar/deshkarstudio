package com.crm.deshkarStudio.services;

import com.crm.deshkarStudio.dto.LoginRequest;
import com.crm.deshkarStudio.dto.SignupRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface AuthService {
    ResponseEntity<?> signUp(SignupRequest req);
    ResponseEntity<?> login(LoginRequest req);
    ResponseEntity<Map<String, String>> requestOtp(String phoneNumber);
    ResponseEntity<Map<String, String>> verifyOtp(String phoneNumber, String otp);
}
