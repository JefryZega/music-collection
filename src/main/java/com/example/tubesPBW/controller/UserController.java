package com.example.tubesPBW.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public class UserController {
    @GetMapping("/home")
    public String registerView(Model model) {
        return "layout/homepage-all";
    }

    // TAMPILAN PROFILE ARTIST
    @GetMapping("/artist")
    public String artist() {
        return "layout/artist";
    }

    // TAMPILAN ALBUM
    @GetMapping("/album")
    public String homeAlbum() {
        return "layout/album";
    }

    // TAMPILAN DAFTAR ALBUM YANG DIMILIK ARTIST TERTENTU
    @GetMapping("/artist/album")
    public String homeArtistAlbum() {
        return "layout/artist-album";
    }


}
