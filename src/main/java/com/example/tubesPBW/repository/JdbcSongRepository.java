// JdbcSongRepository.java - Implementasi dengan JDBC Template
package com.example.tubesPBW.repository;

import com.example.tubesPBW.model.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcSongRepository implements SongRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    private final RowMapper<Song> songRowMapper = new RowMapper<Song>() {
        @Override
        public Song mapRow(ResultSet rs, int rowNum) throws SQLException {
            Song song = new Song();
            song.setSongID(rs.getLong("songID"));
            song.setTitle(rs.getString("title"));
            song.setArtistName(rs.getString("artistName"));
            song.setAlbumTitle(rs.getString("albumTitle"));
            song.setArtistID(rs.getLong("artistID"));
            song.setAlbumID(rs.getLong("albumID")); // Pastikan ada
            if (columnExists(rs, "album_art")) {
                song.setAlbumArt(rs.getString("album_art"));
            } else {
                song.setAlbumArt("/assets/img/album-art/default.jpg");
            }
            return song;
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
    public List<Song> findAll() {
        String sql = "SELECT s.songID, s.title, " +
                    "a.artistName, al.albumTitle, a.artistID, " +
                    "s.albumID, al.album_art " +
                    "FROM song s " +
                    "JOIN album al ON s.albumID = al.albumID " +
                    "JOIN artist a ON al.artistID = a.artistID " +
                    "ORDER BY s.title";
        return jdbcTemplate.query(sql, songRowMapper);
    }
    
    @Override
    public Optional<Song> findById(Long id) {
        String sql = "SELECT s.songID, s.title, " +
                    "a.artistName, al.albumTitle, a.artistID, " +
                    "s.albumID, al.album_art " + 
                    "FROM song s " +
                    "JOIN album al ON s.albumID = al.albumID " +
                    "JOIN artist a ON al.artistID = a.artistID " +
                    "WHERE s.songID = ?";
        List<Song> results = jdbcTemplate.query(sql, songRowMapper, id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
    
    @Override
    public List<Song> findByTitleContaining(String keyword) {
        String sql = "SELECT s.songID, s.title, " +
                    "a.artistName, al.albumTitle, a.artistID, s.albumID, al.album_art " +
                    "FROM song s " +
                    "JOIN album al ON s.albumID = al.albumID " +
                    "JOIN artist a ON al.artistID = a.artistID " +
                    "WHERE LOWER(s.title) LIKE LOWER(?) " +
                    "ORDER BY s.title";
        return jdbcTemplate.query(sql, songRowMapper, "%" + keyword + "%");
    }
    
    @Override
    public List<Song> findByArtistNameContaining(String keyword) {
        String sql = "SELECT s.songID, s.title, " +
                    "a.artistName, al.albumTitle, a.artistID, s.albumID, al.album_art " +
                    "FROM song s " +
                    "JOIN album al ON s.albumID = al.albumID " +
                    "JOIN artist a ON al.artistID = a.artistID " +
                    "WHERE LOWER(a.artistName) LIKE LOWER(?) " +
                    "ORDER BY s.title";
        return jdbcTemplate.query(sql, songRowMapper, "%" + keyword + "%");
    }
    
    @Override
    public List<Song> findByAlbumTitleContaining(String keyword) {
        String sql = "SELECT s.songID, s.title, " +
                    "a.artistName, al.albumTitle, a.artistID, s.albumID, al.album_art " +
                    "FROM song s " +
                    "JOIN album al ON s.albumID = al.albumID " +
                    "JOIN artist a ON al.artistID = a.artistID " +
                    "WHERE LOWER(al.albumTitle) LIKE LOWER(?) " +
                    "ORDER BY s.title";
        return jdbcTemplate.query(sql, songRowMapper, "%" + keyword + "%");
    }
    
    @Override
    public List<Song> searchByKeyword(String keyword) {
        String sql = "SELECT s.songID, s.title, " +
                    "a.artistName, al.albumTitle, a.artistID, s.albumID, al.album_art " +
                    "FROM song s " +
                    "JOIN album al ON s.albumID = al.albumID " +
                    "JOIN artist a ON al.artistID = a.artistID " +
                    "WHERE LOWER(s.title) LIKE LOWER(?) " +
                    "OR LOWER(a.artistName) LIKE LOWER(?) " +
                    "OR LOWER(al.albumTitle) LIKE LOWER(?) " +
                    "ORDER BY s.title";
        String searchPattern = "%" + keyword + "%";
        return jdbcTemplate.query(sql, songRowMapper, 
            searchPattern, searchPattern, searchPattern);
    }

    @Override
    public List<Song> findSongsByIds(List<Long> songIds) {
        if (songIds == null || songIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        String placeholders = String.join(",", Collections.nCopies(songIds.size(), "?"));
        String sql = "SELECT s.songID, s.title, " +
                    "a.artistName, al.albumTitle, a.artistID, s.albumID, al.album_art " +
                    "FROM song s " +
                    "JOIN album al ON s.albumID = al.albumID " +
                    "JOIN artist a ON al.artistID = a.artistID " +
                    "WHERE s.songID IN (" + placeholders + ") " +
                    "ORDER BY s.title";
        return jdbcTemplate.query(sql, songRowMapper, songIds.toArray());
    }

    @Override
    public List<Song> findSongsByArtistId(Long artistId) {
        String sql = "SELECT s.songID, s.title, " +
                    "a.artistName, al.albumTitle, a.artistID, s.albumID, al.album_art " +
                    "FROM song s " +
                    "JOIN album al ON s.albumID = al.albumID " +
                    "JOIN artist a ON al.artistID = a.artistID " +
                    "WHERE a.artistID = ? " +
                    "ORDER BY s.title";
        
        return jdbcTemplate.query(sql, songRowMapper, artistId);
    }

    @Override
    public List<Song> findSongsByAlbumId(Long albumId) {
        String sql = "SELECT s.songid, s.title, " +
                    "a.artistname, al.albumtitle, a.artistid, s.albumID, al.album_art " +
                    "FROM song s " +
                    "JOIN album al ON s.albumid = al.albumid " +
                    "JOIN artist a ON al.artistid = a.artistid " +
                    "WHERE s.albumid = ? " +
                    "ORDER BY s.title";
        
        return jdbcTemplate.query(sql, songRowMapper, albumId);
    }

    @Override
    public List<Song> findTop10Weekly() {
        String sql = "SELECT s.songID, s.title, a.artistName, al.albumTitle, al.album_art, " +
                     "COUNT(f.favouritesID) as total_likes " +
                     "FROM favourites f " +
                     "JOIN song s ON f.songID = s.songID " +
                     "JOIN album al ON s.albumID = al.albumID " +
                     "JOIN artist a ON al.artistID = a.artistID " +
                     "WHERE f.added_at >= NOW() - INTERVAL '7 days' " + 
                     "GROUP BY s.songID, s.title, a.artistName, al.albumTitle, al.album_art " +
                     "ORDER BY total_likes DESC " +
                     "LIMIT 10";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Song song = new Song();
            song.setSongID(rs.getLong("songID"));
            song.setTitle(rs.getString("title"));
            song.setArtistName(rs.getString("artistName"));
            song.setAlbumTitle(rs.getString("albumTitle"));
            
            // Mapping kolom baru
            song.setAlbumArt(rs.getString("album_art")); 
            song.setWeeklyLikes(rs.getLong("total_likes"));
            
            return song;
        });
    }

    @Override
    public void save(Song song) {
        String sql = "INSERT INTO song (title, albumID) VALUES (?, ?)";
        jdbcTemplate.update(sql, song.getTitle(), song.getAlbumID());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM song WHERE songID = ?";
        jdbcTemplate.update(sql, id);
    }
}