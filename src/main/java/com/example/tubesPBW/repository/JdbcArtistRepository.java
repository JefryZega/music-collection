package com.example.tubesPBW.repository;

import com.example.tubesPBW.model.Artist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcArtistRepository implements ArtistRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    private final RowMapper<Artist> artistRowMapper = new RowMapper<Artist>() {
        @Override
        public Artist mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Artist(
                rs.getLong("artistID"),
                rs.getString("artistName"),
                rs.getString("artistProfile")
            );
        }
    };
    
    @Override
    public Optional<Artist> findById(Long artistId) {
        String sql = "SELECT * FROM artist WHERE artistID = ?";
        try {
            Artist artist = jdbcTemplate.queryForObject(sql, artistRowMapper, artistId);
            return Optional.ofNullable(artist);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    @Override
    public List<Artist> findAll() {
        String sql = "SELECT * FROM artist ORDER BY artistName";
        return jdbcTemplate.query(sql, artistRowMapper);
    }
    
    @Override
    public List<Artist> findByArtistNameContaining(String keyword) {
        String sql = "SELECT * FROM artist WHERE LOWER(artistName) LIKE LOWER(?) ORDER BY artistName";
        return jdbcTemplate.query(sql, artistRowMapper, "%" + keyword + "%");
    }

    @Override
    public Optional<Artist> findByName(String artistName) {
        String sql = "SELECT * FROM artist WHERE LOWER(artistName) = LOWER(?)";
        List<Artist> results = jdbcTemplate.query(sql, artistRowMapper, artistName);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public Long save(Artist artist) {
        String sql = "INSERT INTO artist (artistName, artistProfile) VALUES (?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"artistid"});
            
            ps.setString(1, artist.getArtistName());
            ps.setString(2, artist.getArtistProfile());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue(); 
    }
}