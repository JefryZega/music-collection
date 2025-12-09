package com.example.tubesPBW.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberController {
    
    @GetMapping("/member/home")
    public String memberHome() {
        return "/member/home-member";
    }

    @GetMapping("/member/profile/collection")
    public String memberProfileCollection() {
        return "/member/profile-member-collection";
    }

    @GetMapping("/member/profile/collection/song")
    public String memberProfileCollectionSong() {
        return "/member/profile-member-song-list";
    }
    
    @GetMapping("/member/profile/favorite")
    public String memberProfileFavorite() {
        return "/member/profile-member-favorite";
    }

    @GetMapping("/member/profile/favorite/song")
    public String memberProfileFavoriteSong() {
        return "/member/profile-member-favorite-song";
    }

    @GetMapping("/member/profile/upload")
    public String memberUpload() {
        return "/member/uploadMember";
    }
    
    
}
