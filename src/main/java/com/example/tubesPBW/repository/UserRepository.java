package com.example.tubesPBW.repository;

import com.example.tubesPBW.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository {
    private JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        return new User(
            rs.getLong("userID"),
            rs.getString("name"),
            rs.getString("username"),
            rs.getString("email"),
            rs.getString("password_hash"),
            rs.getString("role")
        );
    };
    
    public boolean existsByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return count != null && count > 0;
    }

    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, userRowMapper, username);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, userRowMapper, email);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void save(String name, String username, String email, String passwordHash) {
        String sql = "INSERT INTO users (name, username, email, password_hash, role) VALUES (?, ?, ?, ?, 'member')";
        jdbcTemplate.update(sql, name, username, email, passwordHash);
    }

    // ini buat reset password
    public void update(User user) {
        String sql = "UPDATE users SET name = ?, username = ?, email = ?, password_hash = ?, role = ? WHERE userID = ?";
        jdbcTemplate.update(sql, 
            user.getName(), 
            user.getUsername(), 
            user.getEmail(), 
            user.getPassword(),
            user.getRole(),
            user.getUserID()
        );
    }
}