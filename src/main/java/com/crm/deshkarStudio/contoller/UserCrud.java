package com.crm.deshkarStudio.contoller;

import com.crm.deshkarStudio.dto.SignupRequest;
import com.crm.deshkarStudio.model.User;
import lombok.Getter;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin")
public class UserCrud {

    @PostMapping
    public User createUser(SignupRequest req){
        return null;
    }

    @DeleteMapping
    public void DeleteUser(String username){

    }

    @GetMapping("/users")
    public List<User> getAllUsers(){
        return null;
    }

    public User getSingleUser(String username){
        return null;
    }
}
