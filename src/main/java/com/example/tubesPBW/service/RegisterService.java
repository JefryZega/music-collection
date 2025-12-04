package com.example.tubesPBW.service;

import com.example.tubesPBW.model.RegisterForm;
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
        userRepository.save(form.getName(), form.getUsername(), form.getEmail(), hashedPassword);
        return true;
    }
}