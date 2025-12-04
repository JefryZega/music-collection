package com.example.tubesPBW.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public class UserController {
    @GetMapping("/home")
    public String registerView(Model model) {
        return "homepage-all";
    }
}
