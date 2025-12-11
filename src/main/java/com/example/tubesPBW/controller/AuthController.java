package com.example.tubesPBW.controller;

import com.example.tubesPBW.model.RegisterForm;
import com.example.tubesPBW.model.Song;
import com.example.tubesPBW.model.User; 
import com.example.tubesPBW.service.LoginService; 
import com.example.tubesPBW.service.RegisterService;
import com.example.tubesPBW.service.SongService;
import com.example.tubesPBW.service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private RegisterService registerService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserService userService;

    @Autowired
    private SongService songService;

    @GetMapping("/")
    public String registerView(Model model) {
        model.addAttribute("registerForm", new RegisterForm());
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String homepage(Model model) {
        List<Song> topSongs = songService.getTop10WeeklySongs();
        model.addAttribute("topSongs", topSongs);
        return "layout/homepage-all";
    }

    @GetMapping("/register") 
    public String showRegisterForm(Model model) {
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
    public String loginView() {
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model, HttpSession session,HttpServletResponse response) throws IOException {
        User user = userService.authenticate(username, password);
        if (user == null) {
            model.addAttribute("error", "Invalid username or password");
            return "auth/login";
        }
        session.setAttribute("user", user);
        session.setAttribute("userId", user.getUserID());
        session.setAttribute("userRole", user.getRole());
        if ("admin".equals(user.getRole())) {
            response.sendRedirect("/admin/dashboard");
            return null;
        } else {
            response.sendRedirect("/member/home");
            return null;
        }
    }

    @GetMapping("/reset-password")
    public String resetPasswordView() {
        return "auth/resetPassword";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("email") String email, @RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, RedirectAttributes redirectAttributes) {
        boolean isSuccess = userService.resetPassword(email, oldPassword, newPassword);
        if (isSuccess) {
            redirectAttributes.addFlashAttribute("success", "Password berhasil diubah! Silakan login.");
            return "redirect:/login";
        } else {
            redirectAttributes.addFlashAttribute("error", "Email tidak ditemukan atau Password Lama salah!");
            return "redirect:/reset-password"; 
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}