package com.example.tubesPBW.controller;

import com.example.tubesPBW.model.RegisterForm;
import com.example.tubesPBW.model.User; // Import Model User
import com.example.tubesPBW.service.LoginService; // Import LoginService
import com.example.tubesPBW.service.RegisterService;

import jakarta.servlet.http.HttpSession; // Import Session
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam; // Import RequestParam

@Controller
public class AuthController {

    @Autowired
    private RegisterService registerService;

    @Autowired
    private LoginService loginService; // Inject Service baru

    // ... method registerView dan register yang lama biarkan ...

    @GetMapping("/")
    public String registerView(Model model) {
        model.addAttribute("registerForm", new RegisterForm());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(RegisterForm form, Model model) {
        // ... (Kode validasi yang kamu buat sebelumnya tetap disini) ...
        
        // Kode Register Service
        boolean success = registerService.register(form);
        if (!success) {
            model.addAttribute("error", "Username or email already exists");
            return "auth/register";
        }
        return "redirect:/login";
    }

    // --- TAMBAHAN BAGIAN LOGIN ---

    @GetMapping("/login")
    public String loginView() {
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, 
                        @RequestParam String password, 
                        Model model, 
                        HttpSession session) {
        
        User user = loginService.login(username, password);

        if (user == null) {
            model.addAttribute("error", "Invalid username or password");
            return "auth/login";
        }

        // Simpan user ke session
        session.setAttribute("user", user);

        // Redirect sesuai role (Opsional, sesuaikan dengan kebutuhanmu)
        if ("admin".equals(user.getRole())) {
            return "redirect:/admin/dashboard"; // Pastikan route ini ada
        } else {
            return "redirect:/home"; // Pastikan route ini ada
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}