package com.example.tubesPBW.service;

import com.example.tubesPBW.model.User;
import com.example.tubesPBW.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    private BCryptPasswordEncoder passwordEncoder;
    
    public UserService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
    
    public boolean registerUser(User user) {
        if (userRepository.existsByUsername(user.getUsername()) ||
            userRepository.existsByEmail(user.getEmail())) {
            return false;
        }
        
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("member");
        }
        
        userRepository.save(user);
        return true;
    }
    
    public User authenticate(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user;
            }
        }
        
        return null;
    }
    
    public boolean resetPassword(String email, String oldPassword, String newPassword) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        
        if (userOptional.isEmpty()) {
            return false;
        }
        
        User user = userOptional.get();
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return false;
        }
        String hashedNewPassword = passwordEncoder.encode(newPassword);
        user.setPassword(hashedNewPassword);
        userRepository.update(user);
        return true;
    }
    
    public boolean updatePassword(Long userId, String newPassword) {
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String hashedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(hashedPassword);
            userRepository.update(user);
            return true;
        }
        
        return false;
    }
    
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }
    
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public boolean updateUserProfile(Long userId, String name, String email) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (!user.getEmail().equals(email) && userRepository.existsByEmail(email)) {
                return false;
            }
            user.setName(name);
            user.setEmail(email);
            userRepository.update(user);
            return true;
        }
        
        return false;
    }
    
    public boolean updateUser(User user) {
        Optional<User> existingUser = userRepository.findById(user.getUserID());
        
        if (existingUser.isPresent()) {
            User currentUser = existingUser.get();
            
            if (!currentUser.getUsername().equals(user.getUsername()) &&
                userRepository.existsByUsername(user.getUsername())) {
                return false;
            }
            
            if (!currentUser.getEmail().equals(user.getEmail()) &&
                userRepository.existsByEmail(user.getEmail())) {
                return false; 
            }
            
            userRepository.update(user);
            return true;
        }
        
        return false;
    }
    
    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }
    
    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }
    
    public boolean validateCredentials(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return passwordEncoder.matches(password, user.getPassword());
        }
        
        return false;
    }
    
    public boolean updateUserRole(Long userId, String newRole) {
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            
            if (!"member".equals(newRole) && !"admin".equals(newRole)) {
                return false;
            }
            
            user.setRole(newRole);
            userRepository.update(user);
            return true;
        }
        
        return false;
    }
    
    public String getUserRole(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        return userOpt.map(User::getRole).orElse(null);
    }
    
    public boolean userExists(Long userId) {
        return userRepository.findById(userId).isPresent();
    }
    
    public Long getUserIdByUsername(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        return userOpt.map(User::getUserID).orElse(null);
    }
    
    public String getUserNameById(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        return userOpt.map(User::getName).orElse("Unknown User");
    }
}