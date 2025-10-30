package com.crm.deshkarStudio.contoller;

import com.crm.deshkarStudio.model.User;
import com.crm.deshkarStudio.repo.UserRepo;
import com.crm.deshkarStudio.services.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepo userRepo;
    private final JwtUtil jwtUtil;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(HttpServletRequest request) {
        String token = extractToken(request);
        String username = jwtUtil.getUsernameFromToken(token);

        return userRepo.findByUsername(username)
                .map(user -> ResponseEntity.ok(Map.of(
                        "username", user.getUsername(),
                        "email", user.getEmail(),
                        "phone", user.getPhone(),
                        "role", user.getRole().name()
                )))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(HttpServletRequest request, @RequestBody Map<String, String> body) {
        String token = extractToken(request);
        String username = jwtUtil.getUsernameFromToken(token);

        return userRepo.findByUsername(username)
                .map(user -> {
                    if (body.containsKey("email")) user.setEmail(body.get("email"));
                    if (body.containsKey("phone")) user.setPhone(body.get("phone"));
                    userRepo.save(user);
                    return ResponseEntity.ok(Map.of("message", "Profile updated successfully"));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        return (header != null && header.startsWith("Bearer ")) ? header.substring(7) : null;
    }
}
