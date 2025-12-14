package com.example.tubesPBW.service;

import com.example.tubesPBW.model.Album;
import com.example.tubesPBW.model.Artist;
import com.example.tubesPBW.model.Favorite;
import com.example.tubesPBW.model.Song;
import com.example.tubesPBW.repository.SongRepository;
import com.example.tubesPBW.repository.AlbumRepository;
import com.example.tubesPBW.repository.ArtistRepository;
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

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private AlbumRepository albumRepository;
    
    public List<Song> paginate(List<Song> list, int page, int size) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }
        int start = (page - 1) * size;
        if (start < 0) start = 0;
        if (start >= list.size()) return new ArrayList<>();
        int end = Math.min(start + size, list.size());
        return list.subList(start, end);
    }

    public List<Song> getSongsPaged(int page, int size) {
        List<Song> all = getAllSongs();
        return paginate(all, page, size);
    }

    public List<Song> searchSongsPaged(String searchType, String keyword, int page, int size) {
        List<Song> result = searchSongs(searchType, keyword);
        return paginate(result, page, size);
    }

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

    public List<Song> getSongsByArtistId(Long artistId) {
        return songRepository.findSongsByArtistId(artistId);
    }

    public List<Song> getFavoriteSongs(Long userId) {
        List<Long> favoriteSongIds = favoriteRepository.findFavoriteSongIdsByUserId(userId);
        
        if (favoriteSongIds == null || favoriteSongIds.isEmpty()) {
            return new ArrayList<>();
        }
        if (favoriteSongIds.size() == 1) {
            Long singleSongId = favoriteSongIds.get(0);
            Optional<Song> song = songRepository.findById(singleSongId);
            return song.map(List::of).orElse(new ArrayList<>());
        }
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

    public List<Song> getSongsByAlbumId(Long albumId) {
        return songRepository.findSongsByAlbumId(albumId);
    }

    public List<Song> getTop10WeeklySongs() {
        return songRepository.findTop10Weekly();
    }

    public void addSong(Song song) {
        songRepository.save(song); 
    }

    public void addNewSongComplex(String title, String artistName, String albumTitle, String albumArtPath) {
        Optional<Artist> artistOpt = artistRepository.findByName(artistName);
        Long artistId;
        
        if (artistOpt.isPresent()) {
            artistId = artistOpt.get().getArtistID();
        } else {
            Artist newArtist = new Artist();
            newArtist.setArtistName(artistName);
            newArtist.setArtistProfile("/assets/img/artist-profile/default.jpg"); 
            artistId = artistRepository.save(newArtist); 
        }

        Optional<Album> albumOpt = albumRepository.findByTitleAndArtistId(albumTitle, artistId);
        Long albumId;
        
        if (albumOpt.isPresent()) {
            albumId = albumOpt.get().getAlbumID();
        } else {
            Album newAlbum = new Album();
            newAlbum.setAlbumTitle(albumTitle);
            newAlbum.setArtistID(artistId);
            newAlbum.setAlbumArt(albumArtPath); 
            albumId = albumRepository.save(newAlbum); 
        }

        if (songRepository.isSongExist(title, albumId)) {
            throw new RuntimeException("Lagu '" + title + "' sudah ada di album ini!");
        }

        Song newSong = new Song();
        newSong.setTitle(title);
        newSong.setAlbumID(albumId);
        songRepository.save(newSong);
    }

    public void deleteSong(Long songId) {
        songRepository.deleteById(songId);
    }
}