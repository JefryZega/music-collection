package com.example.tubesPBW.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberController {
    
    // TAMPILAN HOME MEMBER
    @GetMapping("/member/home")
    public String memberHome() {
        return "/member/member-home";
    }

    // TAMPILAN SAAT PROFILE DI KLIK
    @GetMapping("/member/profile")
    public String memberProfile() {
        return "/member/member-profile";
    }

    // TAMPILAN TAB FAVORITE
    @GetMapping("/member/profile/favorite")
    public String memberProfileFavorite() {
        return "/member/member-profile-favorite";
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
