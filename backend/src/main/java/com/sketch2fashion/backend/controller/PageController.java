package com.sketch2fashion.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class PageController {

    @GetMapping("/upload")
    public String uploadForm() {
        return "upload";
    }

    @GetMapping("/guide")
    public String guide() {
        return "guide";
    }
}
