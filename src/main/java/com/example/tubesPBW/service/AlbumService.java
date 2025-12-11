// AlbumService.java
package com.example.tubesPBW.service;

import com.example.tubesPBW.model.Album;
import com.example.tubesPBW.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AlbumService {
    
    @Autowired
    private AlbumRepository albumRepository;
    
    public List<Album> getAllAlbums() {
        return albumRepository.findAll();
    }
    
    public Album getAlbumById(Long albumId) {
        return albumRepository.findById(albumId).orElse(null);
    }
    
    public List<Album> getAlbumsByArtistId(Long artistId) {
        return albumRepository.findByArtistId(artistId);
    }
    
    public List<Album> searchAlbums(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllAlbums();
        }
        return albumRepository.findByAlbumTitleContaining(keyword.trim());
    }
    
    public String getAlbumArtFilename(String albumTitle) {
        if (albumTitle == null || albumTitle.trim().isEmpty()) {
            return "default.png";
        }
        
        String filename = albumTitle.toLowerCase()
            .replace(" ", "-")
            .replace("/", "-")
            .replace("&", "and")
            .replace("'", "")
            .replaceAll("[^a-z0-9-]", "");
        
        return "album-" + filename + ".png";
    }
}