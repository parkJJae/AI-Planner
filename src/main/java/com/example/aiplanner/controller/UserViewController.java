package com.example.aiplanner.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserViewController {

    @GetMapping("users/register")
    public String showRegisterPage() {
        return "signup"; // signup.html 파일을 반환
    }

    @GetMapping("users/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/planner-create")
    public String showPlannerCreatePage() {
        return "planner-create";
    }
    @GetMapping("/planner-generate")
    public ModelAndView showPlannerForm() {
        return new ModelAndView("planner-generate"); // templates/planner-generate.html 반환
    }
}
