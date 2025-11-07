package com.crm.deshkarStudio.contoller;


import com.crm.deshkarStudio.dto.JwtResponse;
import com.crm.deshkarStudio.dto.LoginRequest;
import com.crm.deshkarStudio.dto.ResetPassword;
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

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public  ResponseEntity<?> login(@RequestBody LoginRequest req){
        log.info(req.toString());
        return authService.login(req);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user){
        log.info("User: " + user.toString());
        return authService.signup(user);
    }

    @PostMapping("/first-login")
    public ResponseEntity<?> firstLogin(@RequestBody ResetPassword req){
        return authService.firstLogin(req);
    }

    @PostMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestBody Map<String, String> map){
        String token = map.get("token");
        System.out.println("Token in Controller: " + map.get("token"));
        System.out.println(token.getClass());
        boolean isValid = authService.validateToken(token);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("isValid", isValid));


    }
}