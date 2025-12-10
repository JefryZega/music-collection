package com.example.tubesPBW.repository;

import com.example.tubesPBW.model.Favorite;
import java.util.List;
import java.util.Optional;

public interface FavoriteRepository {
    List<Favorite> findByUserId(Long userId);
    List<Long> findFavoriteSongIdsByUserId(Long userId);
    Optional<Favorite> findByUserIdAndSongId(Long userId, Long songId);
    void addFavorite(Favorite favorite);
    void removeFavorite(Long userId, Long songId);
    boolean isFavorite(Long userId, Long songId);
}