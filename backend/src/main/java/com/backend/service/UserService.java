package com.backend.service;

import com.backend.entity.User;
import com.backend.repository.UserRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    // Method to register a new user
    public String registerUser(User user) {
        // Check if user already exists by username
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return "email already taken!";
        }
        user.setDebt(0);
        if (user.getAdminId() == null)
            user.setAdminId(1);
        userRepository.save(user);
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

    public User findById(Integer userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("No Such User"));
    }
}
