package com.sketch2fashion.backend.controller;

import com.sketch2fashion.backend.service.GalleryService;
import com.sketch2fashion.backend.service.dto.GallerysResponseDto;
import com.sketch2fashion.backend.service.dto.SearchsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gallery")
public class GalleryController {

    private final GalleryService galleryService;

    @GetMapping
    public ResponseEntity<GallerysResponseDto> findGallery() {
        return ResponseEntity.ok(galleryService.findAllGallery());
    }

    @GetMapping("/{resultId}")
    public ResponseEntity<SearchsResponseDto> findSearchs(@PathVariable("resultId") Long resultId) {
        return ResponseEntity.ok(galleryService.findAllSearch(resultId));
    }
}
