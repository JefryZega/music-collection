package com.example.tubesPBW.service;

import com.example.tubesPBW.model.User;
import com.example.tubesPBW.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public User login(String username, String rawPassword) {
        // 1. Cari user di database
        Optional<User> userOptional = userRepository.findByUsername(username);

        // 2. Kalau user tidak ketemu, return null
        if (userOptional.isEmpty()) {
            return null;
        }

        User user = userOptional.get();

        // 3. Cek apakah password cocok menggunakan BCrypt
        if (passwordEncoder.matches(rawPassword, user.getPassword())) {
            return user; // Password benar
        }

        return null; // Password salah
    }
}