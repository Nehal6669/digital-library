package com.example.digital_library.controller;  // 👈 keep this consistent with your project structure

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "📚 Welcome to the Digital Library API!";
    }
}
