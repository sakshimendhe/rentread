package com.example.rentread.service;

import com.example.rentread.model.User;
import com.example.rentread.model.Role;
import com.example.rentread.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;  // Using PasswordEncoder instead of BCryptPasswordEncoder

    public User registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
    
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }
    
        return userRepository.save(user);
    }
    

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}
