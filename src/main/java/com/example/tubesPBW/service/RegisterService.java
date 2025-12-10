package com.example.tubesPBW.service;

import com.example.tubesPBW.model.RegisterForm;
import com.example.tubesPBW.model.User;
import com.example.tubesPBW.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public RegisterService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public boolean register(RegisterForm form) {
        if (userRepository.existsByUsername(form.getUsername()) ||
            userRepository.existsByEmail(form.getEmail())) {
            return false; 
        }

        String hashedPassword = passwordEncoder.encode(form.getPassword());
        User newUser = new User();
        newUser.setName(form.getName());
        newUser.setUsername(form.getUsername());
        newUser.setEmail(form.getEmail());
        newUser.setPassword(hashedPassword);
        newUser.setRole("member");

        userRepository.save(newUser);
        return true;
    }
}