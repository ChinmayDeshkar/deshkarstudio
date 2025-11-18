package com.crm.deshkarStudio.services;

import com.crm.deshkarStudio.dto.LoginRequest;
import com.crm.deshkarStudio.dto.ResetPassword;
import com.crm.deshkarStudio.dto.SignupRequest;
import com.crm.deshkarStudio.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface AuthService {

    ResponseEntity<?> login(LoginRequest req);

    ResponseEntity<?> signup(User user);

    ResponseEntity<?> firstLogin(ResetPassword req);

    Boolean validateToken(String token);

    ResponseEntity<?> verifyOtp(Map<String, String> req);
}
