package com.example.tubesPBW.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    
    @GetMapping("/admin/home")
    public String adminHome() {
        return "admin/home-admin";
    }

    @GetMapping("/admin/profile")
    public String adminProfile() {
        return "admin/admin-profile";
    }
}