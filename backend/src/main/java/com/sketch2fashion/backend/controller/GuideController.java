package com.sketch2fashion.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GuideController {

    @GetMapping("/api/guide")
    public String guide() {
        return "guide";
    }
}
