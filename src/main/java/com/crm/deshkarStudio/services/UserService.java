package com.crm.deshkarStudio.services;

import com.crm.deshkarStudio.model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    List<User> getAll();
    User getUserById(String id);
    User createUser(User user);
    int employeeCount();
    User deactivateUser(String id);
    List<User> getAllEmployee();
    User updateEmployee(String id, User updatedUser);

}
