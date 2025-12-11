package com.example.tubesPBW.repository;

import com.example.tubesPBW.model.Favorite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcFavoriteRepository implements FavoriteRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    private final RowMapper<Favorite> favoriteRowMapper = new RowMapper<Favorite>() {
        @Override
        public Favorite mapRow(ResultSet rs, int rowNum) throws SQLException {
            Favorite favorite = new Favorite(
                rs.getLong("userID"),
                rs.getLong("songID")
            );
            favorite.setFavoritesID(rs.getLong("favouritesID"));
            
            Timestamp timestamp = rs.getTimestamp("added_at");
            if (timestamp != null) {
                favorite.setAddedAt(timestamp.toLocalDateTime());
            }
            
            return favorite;
        }
    };
    
    @Override
    public List<Favorite> findByUserId(Long userId) {
        String sql = "SELECT * FROM favourites WHERE userID = ? ORDER BY added_at DESC";
        return jdbcTemplate.query(sql, favoriteRowMapper, userId);
    }
    
    @Override
    public List<Long> findFavoriteSongIdsByUserId(Long userId) {
        String sql = "SELECT songID FROM favourites WHERE userID = ?";
        return jdbcTemplate.queryForList(sql, Long.class, userId);
    }
    
    @Override
    public Optional<Favorite> findByUserIdAndSongId(Long userId, Long songId) {
        String sql = "SELECT * FROM favourites WHERE userID = ? AND songID = ?";
        List<Favorite> favorites = jdbcTemplate.query(sql, favoriteRowMapper, userId, songId);
        return favorites.isEmpty() ? Optional.empty() : Optional.of(favorites.get(0));
    }
    
    @Override
    public void addFavorite(Favorite favorite) {
        String sql = "INSERT INTO favourites (userID, songID, added_at) VALUES (?, ?, ?) " +
                    "ON CONFLICT (userID, songID) DO NOTHING";
        
        jdbcTemplate.update(sql,
            favorite.getUserID(),
            favorite.getSongID(),
            Timestamp.valueOf(favorite.getAddedAt())
        );
    }
    
    @Override
    public void removeFavorite(Long userId, Long songId) {
        String sql = "DELETE FROM favourites WHERE userID = ? AND songID = ?";
        jdbcTemplate.update(sql, userId, songId);
    }
    
    @Override
    public boolean isFavorite(Long userId, Long songId) {
        String sql = "SELECT COUNT(*) FROM favourites WHERE userID = ? AND songID = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId, songId);
        return count != null && count > 0;
    }
}