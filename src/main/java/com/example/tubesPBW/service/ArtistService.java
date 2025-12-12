package com.example.tubesPBW.service;

import com.example.tubesPBW.model.Artist;
import com.example.tubesPBW.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArtistService {
    
    @Autowired
    private ArtistRepository artistRepository;
    
    public Artist getArtistById(Long artistId) {
        Optional<Artist> artistOpt = artistRepository.findById(artistId);
        return artistOpt.orElse(null);
    }
    
    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
    }
    
    public List<Artist> searchArtists(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllArtists();
        }
        return artistRepository.findByArtistNameContaining(keyword.trim());
    }

    public void validateArtist(Artist artist){
        if (artist.getArtistName() == null || artist.getArtistName().trim().isEmpty()) {
            throw new IllegalArgumentException("Artist name is required");
        }
    }
}