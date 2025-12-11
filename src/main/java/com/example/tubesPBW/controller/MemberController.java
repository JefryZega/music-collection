package com.example.tubesPBW.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.tubesPBW.service.SongService;
import com.example.tubesPBW.service.UserService;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

import com.example.tubesPBW.model.Favorite;
import com.example.tubesPBW.model.Song;
import com.example.tubesPBW.model.User;

@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private SongService songService;

    @Autowired
    private UserService userService;

    @GetMapping("/home")
    public String memberHome(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        Long artistId = (long) 6;
        model.addAttribute("artistId", artistId);
        model.addAttribute("user", user);
        return "/member/member-home";
    }

    @GetMapping("/profile")
    public String memberProfile(@RequestParam(name = "search", required = false) String search, @RequestParam(name = "searchType", defaultValue = "all") String searchType, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        
        List<Song> songs;
        if (search != null && !search.trim().isEmpty()) {
            songs = songService.searchSongs(searchType, search);
        } else {
            songs = songService.getAllSongs();
        }

        List<Song> userFavorites = songService.getFavoriteSongs(user.getUserID());
        List<Long> favoriteSongIds = new ArrayList<>();
        
        for (Song favSong : userFavorites) {
            favoriteSongIds.add(favSong.getSongID());
        }

        for (Song song : songs) {
            song.setFavorite(favoriteSongIds.contains(song.getSongID()));
        }

        model.addAttribute("songs", songs);
        model.addAttribute("searchQuery", search);
        model.addAttribute("searchType", searchType);
        model.addAttribute("totalSongs", songs.size());
        model.addAttribute("user", user);
        model.addAttribute("userName", user.getName());
        model.addAttribute("userRole", user.getRole());
        
        return "/member/member-profile";
    }

    @GetMapping("/profile/favorite")
    public String memberProfileFavorite(@RequestParam(name = "search", required = false) String search, HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        List<Song> favoriteSongs = songService.getFavoriteSongs(user.getUserID());
        
        if (search != null && !search.trim().isEmpty()) {
            String searchLower = search.trim().toLowerCase();
            List<Song> filteredSongs = new ArrayList<>();
            for (Song song : favoriteSongs) {
                if (song.getTitle().toLowerCase().contains(searchLower) ||
                    song.getArtistName().toLowerCase().contains(searchLower) ||
                    song.getAlbumTitle().toLowerCase().contains(searchLower)) {
                    filteredSongs.add(song);
                }
            }
            favoriteSongs = filteredSongs;
        }
        
        for (Song song : favoriteSongs) {
            song.setFavorite(true);
        }
        
        List<Song> allSongs = songService.getAllSongs();
        List<Song> recommendations = new ArrayList<>();
        
        List<Long> favoriteSongIds = new ArrayList<>();
        for (Song favSong : favoriteSongs) {
            favoriteSongIds.add(favSong.getSongID());
        }
        
        for (Song song : allSongs) {
            if (!favoriteSongIds.contains(song.getSongID()) && recommendations.size() < 5) {
                song.setFavorite(false);
                recommendations.add(song);
            }
        }
        
        model.addAttribute("favoriteSongs", favoriteSongs);
        model.addAttribute("totalFavorites", favoriteSongs.size());
        model.addAttribute("recommendations", recommendations);
        model.addAttribute("searchQuery", search);
        model.addAttribute("user", user); 
        model.addAttribute("userName", user.getName());
        model.addAttribute("userRole", user.getRole());
        
        return "/member/member-profile-favorite";
    }

    @PostMapping("/profile/favorite/add")
    public String addFavorite(@RequestParam Long songId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        songService.toggleFavorite(user.getUserID(), songId);
        return "redirect:/member/profile/favorite";
    }

    @PostMapping("/profile/favorite/remove")
    public String removeFavorite(@RequestParam Long songId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        songService.toggleFavorite(user.getUserID(), songId);
        
        return "redirect:/member/profile/favorite";
    }

    @GetMapping("/profile/favorite/song")
    public String memberProfileFavoriteSong(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "/member/profile-member-favorite-song";
    }

    @GetMapping("/profile/upload")
    public String memberUpload(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "/member/uploadMember";
    }

    // BARU, buat search bar
    @GetMapping("/api/search") 
    @ResponseBody // biar outputnya JSON, bukan HTML
    public List<Song> searchSongsApi(@RequestParam("q") String query) {
        return songService.searchSongs("general", query);
    }
}