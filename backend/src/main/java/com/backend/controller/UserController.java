package com.backend.controller;

import com.backend.entity.User;
import com.backend.repository.UserRepository;
import com.backend.service.UserService;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/auth")

public class UserController {
    
    @Autowired
    private UserService userService;


    // Sign-up endpoint
    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        // Register the user in the database
        System.out.println("aaaa");
        String result = userService.registerUser(user);

        if (result.equals("User registered successfully!")) {
            return ResponseEntity.ok("User registered successfully! Please verify your email.");
        }
        return ResponseEntity.status(400).body(result);  // In case of failure
    }

    @GetMapping("/getUserInformation/{userId}")
    public User getUserInformation(@PathVariable Integer userId) {
        try {
            System.out.println(userId);
            return userService.findById(userId);
        } catch (Exception e) {
            return new User();
        }
    }

    @GetMapping("/Login/{email}/{password}")
    public Integer Login(@PathVariable String email, @PathVariable String password) {
        // Register the user in the database
        System.out.println("vdfdf");
        System.out.println("llll");
        System.out.println(password);
        System.out.println(email);

        User user = userService.findByEmailAndPassword(email, password);
        System.out.println(user);
        return user==null?null:user.getIdUser();
    }
}