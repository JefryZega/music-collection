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


import com.example.tubesPBW.service.AlbumService;
import com.example.tubesPBW.service.ArtistService;
import com.example.tubesPBW.service.SongService;
import com.example.tubesPBW.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

import com.example.tubesPBW.annotation.RequiresMember;
import com.example.tubesPBW.model.Album;
import com.example.tubesPBW.model.Artist;
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

    @Autowired
    private ArtistService artistService;

    @Autowired
    private AlbumService albumService;

    @GetMapping("/home")
    @RequiresMember
    public String memberHome(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        List<Artist> artists = artistService.getAllArtists();
        List<Album> albums = albumService.getAllAlbums();
        
        model.addAttribute("artists", artists);
        model.addAttribute("albums", albums);
        model.addAttribute("user", user);
        return "/member/member-home";
    }

    @GetMapping("/profile")
    @RequiresMember
    public String memberProfile(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(name = "search", required = false) String search, @RequestParam(name = "searchType", defaultValue = "all") String searchType, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        List<Song> allSongs;
        if (search != null && !search.trim().isEmpty()) {
            allSongs = songService.searchSongs(searchType, search);
        } else {
            allSongs = songService.getAllSongs();
        }
        int totalSongs = allSongs.size();
        int totalPages = (int) Math.ceil((double) totalSongs / size);

        if (page < 1) page = 1;
        if (page > totalPages && totalPages > 0) page = totalPages;
        List<Song> songs;
        if (totalSongs == 0) {
            songs = new ArrayList<>();
        } else {
            songs = songService.paginate(allSongs, page, size);
        }

        List<Song> userFavorites = songService.getFavoriteSongs(user.getUserID());
        List<Long> favIds = userFavorites.stream().map(Song::getSongID).toList();

        for (Song s : songs) {
            s.setFavorite(favIds.contains(s.getSongID()));
        }

        model.addAttribute("songs", songs);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalSongs", totalSongs);

        model.addAttribute("searchQuery", search);
        model.addAttribute("searchType", searchType);

        model.addAttribute("user", user);
        model.addAttribute("userName", user.getName());
        model.addAttribute("userRole", user.getRole());

        return "/member/member-profile";
    }

    @GetMapping("/profile/favorite")
    @RequiresMember
    public String memberProfileFavorite(@RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "size", defaultValue = "10") int size, @RequestParam(name = "search", required = false) String search, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        List<Song> favoriteSongs = songService.getFavoriteSongs(user.getUserID());
        if (search != null && !search.trim().isEmpty()) {
            String keyword = search.trim().toLowerCase();
            List<Song> filtered = new ArrayList<>();

            for (Song s : favoriteSongs) {
                if (s.getTitle().toLowerCase().contains(keyword)
                        || s.getArtistName().toLowerCase().contains(keyword)
                        || s.getAlbumTitle().toLowerCase().contains(keyword)) {
                    filtered.add(s);
                }
            }
            favoriteSongs = filtered;
        }

        for (Song s : favoriteSongs) {
            s.setFavorite(true);
        }
        int totalFavorites = favoriteSongs.size();
        if (totalFavorites == 0) {
            model.addAttribute("favoriteSongs", new ArrayList<Song>());
            model.addAttribute("totalFavorites", 0);
            model.addAttribute("searchQuery", search);
            model.addAttribute("currentPage", 1);
            model.addAttribute("totalPages", 0);
            
            model.addAttribute("user", user);
            model.addAttribute("userName", user.getName());
            model.addAttribute("userRole", user.getRole());
            List<Song> recommendations = songService.getRecommendationsExcludingFavorites(user.getUserID(), 5);
            model.addAttribute("recommendations", recommendations);
            return "/member/member-profile-favorite";
        }

        int totalPages = (int) Math.ceil((double) totalFavorites / size);

        if (page < 1) page = 1;
        if (page > totalPages) page = totalPages;

        List<Song> pageFavorites = songService.paginate(favoriteSongs, page, size);
        List<Song> recommendations = songService.getRecommendationsExcludingFavorites(user.getUserID(), 5);
        model.addAttribute("favoriteSongs", pageFavorites);
        model.addAttribute("totalFavorites", totalFavorites);
        model.addAttribute("searchQuery", search);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("user", user);
        model.addAttribute("userName", user.getName());
        model.addAttribute("userRole", user.getRole());
        model.addAttribute("recommendations", recommendations);

        return "/member/member-profile-favorite";
    }

    @PostMapping("/profile/favorite/toggle")
    @RequiresMember
    public String toggleFavorite(@RequestParam Long songId, HttpSession session, HttpServletRequest request) {
        User user = (User) session.getAttribute("user");
        songService.toggleFavorite(user.getUserID(), songId);
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/member/profile");
    }

    @PostMapping("/profile/favorite/add")
    @RequiresMember
    public String addFavorite(@RequestParam Long songId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        songService.toggleFavorite(user.getUserID(), songId);
        return "redirect:/member/profile/favorite";
    }

    @PostMapping("/profile/favorite/remove")
    @RequiresMember
    public String removeFavorite(@RequestParam Long songId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        songService.toggleFavorite(user.getUserID(), songId);
        
        return "redirect:/member/profile/favorite";
    }

    @GetMapping("/profile/favorite/song")
    @RequiresMember
    public String memberProfileFavoriteSong(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        model.addAttribute("user", user);
        return "/member/profile-member-favorite-song";
    }

    @GetMapping("/profile/upload")
    @RequiresMember
    public String memberUpload(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        return "/member/uploadMember";
    }

    @GetMapping("/api/search") 
    @ResponseBody
    public List<Song> searchSongsApi(@RequestParam("q") String query) {
        return songService.searchSongs("general", query);
    }
}