package com.example.tubesPBW.repository;

import com.example.tubesPBW.model.User;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long userId);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    void save(User user);
    void update(User user);
}