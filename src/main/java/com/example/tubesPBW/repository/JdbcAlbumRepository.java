package com.example.tubesPBW.repository;

import com.example.tubesPBW.model.Album;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcAlbumRepository implements AlbumRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    private final RowMapper<Album> albumRowMapper = new RowMapper<Album>() {
        @Override
        public Album mapRow(ResultSet rs, int rowNum) throws SQLException {
            Album album = new Album();
            album.setAlbumID(rs.getLong("albumid"));
            album.setAlbumTitle(rs.getString("albumtitle"));
            album.setAlbumArt(rs.getString("album_art"));
            album.setArtistID(rs.getLong("artistid"));
            album.setArtistName(rs.getString("artistname"));

            if (columnExists(rs, "artistprofile")) {
                album.setArtistProfile(rs.getString("artistprofile"));
            } else {
                album.setArtistProfile("/assets/img/artist-profile/default.jpg");
            }
            
            return album;
        }
        
        private boolean columnExists(ResultSet rs, String columnName) {
            try {
                rs.findColumn(columnName);
                return true;
            } catch (SQLException e) {
                return false;
            }
        }
    };
    
    @Override
    public List<Album> findAll() {
        String sql = "SELECT a.*, ar.artistname, ar.artistprofile " +
                    "FROM album a " +
                    "JOIN artist ar ON a.artistid = ar.artistid " +
                    "ORDER BY a.albumtitle";
        return jdbcTemplate.query(sql, albumRowMapper);
    }
    
    @Override
    public Optional<Album> findById(Long albumId) {
        String sql = "SELECT a.*, ar.artistname, ar.artistprofile " + 
                    "FROM album a " +
                    "JOIN artist ar ON a.artistid = ar.artistid " +
                    "WHERE a.albumid = ?";
        List<Album> results = jdbcTemplate.query(sql, albumRowMapper, albumId);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
    
    @Override
    public List<Album> findByArtistId(Long artistId) {
        String sql = "SELECT a.*, ar.artistname, ar.artistprofile " +
                    "FROM album a " +
                    "JOIN artist ar ON a.artistid = ar.artistid " +
                    "WHERE a.artistid = ? " +
                    "ORDER BY a.albumtitle";
        return jdbcTemplate.query(sql, albumRowMapper, artistId);
    }
    
    @Override
    public List<Album> findByAlbumTitleContaining(String keyword) {
        String sql = "SELECT a.*, ar.artistname " +
                    "FROM album a " +
                    "JOIN artist ar ON a.artistid = ar.artistid " +
                    "WHERE LOWER(a.albumtitle) LIKE LOWER(?) " +
                    "ORDER BY a.albumtitle";
        return jdbcTemplate.query(sql, albumRowMapper, "%" + keyword + "%");
    }
    
    @Override
    public List<Album> findByArtistNameContaining(String keyword) {
        String sql = "SELECT a.*, ar.artistname " +
                    "FROM album a " +
                    "JOIN artist ar ON a.artistid = ar.artistid " +
                    "WHERE LOWER(ar.artistname) LIKE LOWER(?) " +
                    "ORDER BY a.albumtitle";
        return jdbcTemplate.query(sql, albumRowMapper, "%" + keyword + "%");
    }
}