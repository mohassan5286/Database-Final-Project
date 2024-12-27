package com.backend.service;

import com.backend.entity.User;
import com.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;


    // Method to register a new user
    public String registerUser(User user) {
        // Check if user already exists by username
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return "email already taken!";
        }
        return "User registered successfully!";
    }

    // Method to get user by username (for login)
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findByEmailAndPassword(String email, String password) {
        System.out.println("user "+ userRepository.findByEmailAndPassword(email, password));
        return   userRepository.findByEmailAndPassword(email, password);
    }
}
