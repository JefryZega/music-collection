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


    // yg diubah
    @GetMapping("/profile")
    public String memberProfile(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "searchType", defaultValue = "all") String searchType,
            HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        List<Song> allSongs;

        // Jika search aktif, gunakan hasil search
        if (search != null && !search.trim().isEmpty()) {
            allSongs = songService.searchSongs(searchType, search);
        } else {
            allSongs = songService.getAllSongs();
        }

        // TOTAL PAGE
        int totalSongs = allSongs.size();
        int totalPages = (int) Math.ceil((double) totalSongs / size);

        // AMBIL PAGE SEKARANG
        List<Song> songs = songService.paginate(allSongs, page, size);

        // Tandai favorite
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
    // batas yg diubah

    @GetMapping("/profile/favorite")
    public String memberProfileFavorite(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "search", required = false) String search,
            HttpSession session,
            Model model) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        // --- AMBIL FAVORITE USER ---
        List<Song> favoriteSongs = songService.getFavoriteSongs(user.getUserID());

        // --- FILTER SEARCH ---
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

        // TANDAI FAVORITE
        for (Song s : favoriteSongs) {
            s.setFavorite(true);
        }

        // --- PAGINATION ---
        int totalFavorites = favoriteSongs.size();
        int totalPages = (int) Math.ceil((double) totalFavorites / size);

        if (page < 1) page = 1;
        if (page > totalPages) page = totalPages;

        int start = (page - 1) * size;
        int end = Math.min(start + size, totalFavorites);

        List<Song> pageFavorites = favoriteSongs.subList(start, end);

        // --- RECOMMENDATION ---
        List<Song> allSongs = songService.getAllSongs();
        List<Long> favIds = favoriteSongs.stream().map(Song::getSongID).toList();

        List<Song> recommendations = new ArrayList<>();
        for (Song s : allSongs) {
            if (!favIds.contains(s.getSongID()) && recommendations.size() < 5) {
                s.setFavorite(false);
                recommendations.add(s);
            }
        }

        // --- KIRIM KE VIEW ---
        model.addAttribute("favoriteSongs", pageFavorites);
        model.addAttribute("totalFavorites", totalFavorites);
        model.addAttribute("searchQuery", search);

        // PAGINATION DATA
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        // USER
        model.addAttribute("user", user);
        model.addAttribute("userName", user.getName());
        model.addAttribute("userRole", user.getRole());

        // Recommendation
        model.addAttribute("recommendations", recommendations);

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