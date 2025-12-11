package com.example.tubesPBW.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.tubesPBW.model.Album;
import com.example.tubesPBW.model.Artist;
import com.example.tubesPBW.model.Song;
import com.example.tubesPBW.service.AlbumService;
import com.example.tubesPBW.service.ArtistService;
import com.example.tubesPBW.service.SongService;

@Controller
public class UserController {

    @Autowired
    private ArtistService artistService;
    
    @Autowired
    private SongService songService;

    @Autowired
    private AlbumService albumService;

    // TAMPILAN PROFILE ARTIST
    @GetMapping("/artist/{artistId}")
    public String artistProfile(@PathVariable Long artistId, Model model) {
        Artist artist = artistService.getArtistById(artistId);
        if (artist == null) {
            return "redirect:/home";
        }
        List<Song> artistSongs = songService.getSongsByArtistId(artistId);
        
        model.addAttribute("artist", artist);
        model.addAttribute("artistSongs", artistSongs);
        
        return "layout/artist";
    }

    // TAMPILAN ALBUM
    @GetMapping("/artist/{artistId}/album")
    public String artistAlbums(@PathVariable Long artistId, Model model) {
        Artist artist = artistService.getArtistById(artistId);
        if (artist == null) {
            return "redirect:/home";
        }
        
        List<Album> artistAlbums = albumService.getAlbumsByArtistId(artistId);
    
        model.addAttribute("artist", artist);
        model.addAttribute("artistAlbums", artistAlbums);
        return "layout/artist-album";
    }

    @GetMapping("/album/{albumId}")
    public String albumDetail(@PathVariable Long albumId, Model model) {
        Album album = albumService.getAlbumById(albumId);
        if (album == null) {
            return "redirect:/artist";
        }
        List<Song> albumSongs = songService.getSongsByAlbumId(albumId);
        
        model.addAttribute("album", album);
        model.addAttribute("albumSongs", albumSongs);
        
        return "layout/album-detail";
    }

    // TAMPILAN DAFTAR ALBUM YANG DIMILIK ARTIST TERTENTU
    @GetMapping("/artist/album")
    public String homeArtistAlbum() {
        return "layout/artist-album";
    }


}
