package com.example.tubesPBW.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.tubesPBW.model.RegisterForm;
import com.example.tubesPBW.service.RegisterService;

@Controller
public class AuthController {

    @Autowired
    private RegisterService registerService;

    @GetMapping("/")
    public String registerView(Model model) {
        model.addAttribute("registerForm", new RegisterForm());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(RegisterForm form, Model model) {
        if (form.getName() == null || form.getName().trim().isEmpty()) {
            model.addAttribute("error", "Name is required");
            return "auth/register";
        }
        if (form.getUsername() == null || form.getUsername().trim().isEmpty()) {
            model.addAttribute("error", "Username is required");
            return "auth/register";
        }
        if (form.getEmail() == null || form.getEmail().trim().isEmpty()) {
            model.addAttribute("error", "Email is required");
            return "auth/register";
        }
        if (form.getPassword() == null || form.getPassword().length() < 6) {
            model.addAttribute("error", "Password must be at least 6 characters");
            return "auth/register";
        }
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            model.addAttribute("error", "Passwords do not match");
            return "auth/register";
        }

        boolean success = registerService.register(form);
        if (!success) {
            model.addAttribute("error", "Username or email already exists");
            return "auth/register";
        }

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginView(){
        return "auth/login";
    }
}