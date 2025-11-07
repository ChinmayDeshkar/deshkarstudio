package com.crm.deshkarStudio.contoller;

import com.crm.deshkarStudio.model.User;
import com.crm.deshkarStudio.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;

    @GetMapping("/all-employee")
    public List<User> getAllEmployee(){
        return userService.getAllEmployee();
    }

    // ✅ Update employee details (email, phone, active status, salary, etc.)
    @PutMapping("/update-employee/{id}")
    public ResponseEntity<?> updateEmployee(
            @PathVariable String id,
            @RequestBody User updatedUser
    ) {

        User updated = userService.updateEmployee(id, updatedUser);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.badRequest().body("Employee not found or update failed");
        }
    }

}
