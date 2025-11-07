package com.crm.deshkarStudio.services.impl;

import com.crm.deshkarStudio.model.User;
import com.crm.deshkarStudio.repo.UserRepo;
import com.crm.deshkarStudio.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepo userRepo;

    @Override
    public List<User> getAll() {
        return userRepo.findAll();
    }

    @Override
    public User getUserById(String id) {
        return userRepo.findById(id).orElseThrow(() -> new RuntimeException("User Not Found!!!!"));
    }

    @Override
    public User createUser(User user) {
        return userRepo.save(user);
    }

    @Override
    public int employeeCount() {
        return getAll().size();
    }

    @Override
    public User deactivateUser(String id) {
//        User user = getUserById(id);
//        user.setActive(false);
//        return userRepo.save(user);

        return null;
    }

    @Override
    public List<User> getAllEmployee() {
        return userRepo.findAll();
    }

    @Override
    public User updateEmployee(String id, User updatedUser) {
        Optional<User> optionalUser = userRepo.findById(id);

        if (optionalUser.isEmpty()) {
            return null;
        }

        User existingUser = optionalUser.get();

        // ✅ Update only allowed fields (ignore password & role)
        if (updatedUser.getFirstName() != null)
            existingUser.setFirstName(updatedUser.getFirstName());

        if (updatedUser.getLastName() != null)
            existingUser.setLastName(updatedUser.getLastName());

        if (updatedUser.getEmail() != null)
            existingUser.setEmail(updatedUser.getEmail());

        if (updatedUser.getPhone() != null)
            existingUser.setPhone(updatedUser.getPhone());

        if (updatedUser.getSalary() != null)
            existingUser.setSalary(updatedUser.getSalary());

        // ✅ Active status
        existingUser.setActive(updatedUser.isActive());

        // ❌ Do not change password or role
        // existingUser.setPassword(existingUser.getPassword());
        // existingUser.setRole(existingUser.getRole());

        return userRepo.save(existingUser);
    }

}
