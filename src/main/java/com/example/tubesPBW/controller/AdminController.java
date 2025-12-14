package com.example.tubesPBW.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.dao.DataAccessException;

import com.example.tubesPBW.service.AlbumService;
import com.example.tubesPBW.service.ArtistService;
import com.example.tubesPBW.service.SongService;
import com.example.tubesPBW.service.UserService;
import com.example.tubesPBW.util.FileUploadUtil;
import com.example.tubesPBW.annotation.RequiresAdmin;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

import com.example.tubesPBW.model.Album;
import com.example.tubesPBW.model.Artist;
import com.example.tubesPBW.model.Favorite;
import com.example.tubesPBW.model.Song;
import com.example.tubesPBW.model.User;


@RequiresAdmin
@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private SongService songService;

    @Autowired
    private UserService userService;

    @Autowired
    private ArtistService artistService;

    @Autowired
    private AlbumService albumService;

    @GetMapping("/home")
    public String adminHome(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        List<Artist> artists = artistService.getAllArtists();
        List<Album> albums = albumService.getAllAlbums();
        List<Song> topSongs = songService.getTop10WeeklySongs();

        if (artists != null) {
            for (Artist a : artists) {
                System.out.println("Artist: " + a.getArtistName() + " (ID: " + a.getArtistID() + ")");
            }
        }
        
        model.addAttribute("artists", artists);
        model.addAttribute("albums", albums);
        model.addAttribute("user", user);
        model.addAttribute("topSongs", topSongs);

        return "admin/admin-home";
    }

    @GetMapping("/profile")
    public String adminProfile (
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
        model.addAttribute("username", user.getName());
        model.addAttribute("role", user.getRole());

        return "/admin/admin-profile";
    }

    @Value("${file.upload-dir}")
    private String uploadDir; // Mengambil path D:/TubesPBW-sebelumnya/images/

    @PostMapping("/song/add")
    public String addSong(
            @RequestParam("song-title") String title,
            @RequestParam("artist-name") String artistName,
            @RequestParam("album-name") String albumTitle,
            @RequestParam("album-art") MultipartFile multipartFile,
            RedirectAttributes redirectAttributes
    ) throws IOException {
        try {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
            String dbPath = "/assets/img/album-art/" + fileName;

            songService.addNewSongComplex(title, artistName, albumTitle, dbPath);
            redirectAttributes.addFlashAttribute("successMessage", "Berhasil menambah lagu!");
            
        } catch (DataAccessException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Database Error: Gagal menyimpan data. lagu sudah ada.");
        }

        return "redirect:/admin/profile";
    }

    @PostMapping("/song/delete")
    public String deleteSong(@RequestParam("songId") Long songId) {
        songService.deleteSong(songId);
        return "redirect:/admin/profile";
    }

    @GetMapping("/poster")
    public String adminPoster(Model model) {
        List<Song> topSongs = songService.getTop10WeeklySongs();
        model.addAttribute("topSongs", topSongs);
        return "/admin/poster";
    }

}