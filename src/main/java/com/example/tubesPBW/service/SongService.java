package com.example.tubesPBW.service;

import com.example.tubesPBW.model.Favorite;
import com.example.tubesPBW.model.Song;
import com.example.tubesPBW.repository.SongRepository;
import com.example.tubesPBW.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SongService {
    
    @Autowired
    private SongRepository songRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;
    
    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }
    
    public List<Song> searchSongs(String searchType, String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllSongs();
        }
        
        keyword = keyword.trim();
        
        switch (searchType) {
            case "title":
                return songRepository.findByTitleContaining(keyword);
            case "artist":
                return songRepository.findByArtistNameContaining(keyword);
            case "album":
                return songRepository.findByAlbumTitleContaining(keyword);
            default:
                return songRepository.searchByKeyword(keyword);
        }
    }

    public List<Song> getFavoriteSongs(Long userId) {
        List<Long> favoriteSongIds = favoriteRepository.findFavoriteSongIdsByUserId(userId);
        return songRepository.findSongsByIds(favoriteSongIds);
    }
    
    public boolean isSongFavorite(Long userId, Long songId) {
        return favoriteRepository.isFavorite(userId, songId);
    }

    public void toggleFavorite(Long userId, Long songId) {
        if (favoriteRepository.isFavorite(userId, songId)) {
            favoriteRepository.removeFavorite(userId, songId);
        } else {
            Favorite favorite = new Favorite(userId, songId);
            favoriteRepository.addFavorite(favorite);
        }
    }

    public Optional<Song> getSongById(Long songId) {
        return songRepository.findById(songId);
    }
    
    public List<Song> getFavoriteSongs(List<Long> favoriteIds) {
        if (favoriteIds == null || favoriteIds.isEmpty()) {
            return new ArrayList<>();
        }
        return songRepository.findSongsByIds(favoriteIds);
    }

    public List<Song> getRecommendationsExcludingFavorites(Long userId, int limit) {
        List<Song> allSongs = getAllSongs();
        List<Long> favoriteIds = favoriteRepository.findFavoriteSongIdsByUserId(userId);
        List<Song> recommendations = new ArrayList<>();
        for (Song song : allSongs) {
            if (favoriteIds.contains(song.getSongID())) {
                continue;
            }
            
            recommendations.add(song);
            if (recommendations.size() >= limit) {
                break;
            }
        }
        return recommendations;
    }
}