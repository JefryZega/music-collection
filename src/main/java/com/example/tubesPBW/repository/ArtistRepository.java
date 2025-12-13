package com.example.tubesPBW.repository;

import com.example.tubesPBW.model.Artist;
import java.util.List;
import java.util.Optional;

public interface ArtistRepository {
    Optional<Artist> findById(Long artistId);
    List<Artist> findAll();
    List<Artist> findByArtistNameContaining(String keyword);

    Optional<Artist> findByName(String artistName);
    Long save(Artist artist);
}