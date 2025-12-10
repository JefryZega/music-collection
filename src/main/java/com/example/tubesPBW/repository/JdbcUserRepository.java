package com.example.tubesPBW.repository;

import com.example.tubesPBW.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class JdbcUserRepository implements UserRepository {
    private JdbcTemplate jdbcTemplate;

    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
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
    
    @Override
    public boolean existsByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return count != null && count > 0;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, userRowMapper, username);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, userRowMapper, email);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public void save(User user) {
        String sql = "INSERT INTO users (name, username, email, password_hash, role) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, 
            user.getName(), 
            user.getUsername(), 
            user.getEmail(), 
            user.getPassword(),
            user.getRole() != null ? user.getRole() : "member"
        );
    }

    @Override
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
    
    @Override
    public Optional<User> findById(Long userId) {
        String sql = "SELECT * FROM users WHERE userID = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, userRowMapper, userId);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}