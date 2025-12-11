package com.example.tubesPBW.repository;

import com.example.tubesPBW.model.Album;
import java.util.List;
import java.util.Optional;

public interface AlbumRepository {
    List<Album> findAll();
    Optional<Album> findById(Long albumId);
    List<Album> findByArtistId(Long artistId);
    List<Album> findByAlbumTitleContaining(String keyword);
    List<Album> findByArtistNameContaining(String keyword);
}