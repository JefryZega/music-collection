package com.example.tubesPBW.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

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
