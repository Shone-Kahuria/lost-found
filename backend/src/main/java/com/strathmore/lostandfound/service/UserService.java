package com.strathmore.lostandfound.service;

import com.strathmore.lostandfound.model.User;
import com.strathmore.lostandfound.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        validateUser(user);
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username is already taken");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email is already in use");
        }
        // Create new User instance and set fields via reflection due to missing setters
        User newUser = new User();
        try {
            Field usernameField = User.class.getDeclaredField("username");
            usernameField.setAccessible(true);
            usernameField.set(newUser, user.getUsername());

            Field emailField = User.class.getDeclaredField("email");
            emailField.setAccessible(true);
            emailField.set(newUser, user.getEmail());

            Field passwordField = User.class.getDeclaredField("password");
            passwordField.setAccessible(true);
            passwordField.set(newUser, passwordEncoder.encode(user.getPassword()));
        } catch (Exception e) {
            throw new RuntimeException("Failed to set user fields", e);
        }
        return userRepository.save(newUser);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    private void validateUser(User user) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        // Add more validations as needed
    }
}
