package com.example.tubesPBW.repository;

import com.example.tubesPBW.model.Song;
import java.util.List;
import java.util.Optional;

public interface SongRepository {
    List<Song> findAll();
    Optional<Song> findById(Long id);
    List<Song> findByTitleContaining(String keyword);
    List<Song> findByArtistNameContaining(String keyword);
    List<Song> findByAlbumTitleContaining(String keyword);
    List<Song> searchByKeyword(String keyword);
    List<Song> findSongsByIds(List<Long> songIds);
    List<Song> findSongsByArtistId(Long artistId);
    List<Song> findSongsByAlbumId(Long albumId);
    List<Song> findTop10Weekly();
    void save(Song song);
    void deleteById(Long id);
}