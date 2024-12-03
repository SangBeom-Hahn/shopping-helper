package com.sketch2fashion.backend.controller;

import com.sketch2fashion.backend.service.ResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class PageController {

    private final ResultService resultService;

    @GetMapping("/home/{messageId}")
    public String home(@PathVariable("messageId") Long messageId) {
        resultService.handlePersistEntity(messageId);
        return "index";
    }

    @GetMapping("/upload")
    public String uploadForm() {
        return "upload";
    }

    @GetMapping("/guide")
    public String guide() {
        return "guide";
    }

    @GetMapping("/result")
    public String result() {
        return "result";
    }

    @GetMapping("/farfetch")
    public String farfetch() {
        return "farfetch";
    }

    @GetMapping("/amazon")
    public String amazon() {
        return "amazon";
    }

    @GetMapping("/pinterest")
    public String pinterest() {
        return "pinterest";
    }

    @GetMapping("/etsy")
    public String etsy() {
        return "etsy";
    }

    @GetMapping("/noFilter")
    public String noFilter() {
        return "noFilter";
    }
}
