package com.example.aiplanner.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserViewController {

    @GetMapping("/register")
    public String showRegisterPage() {
        return "signup"; // signup.html 파일을 반환
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }
}
