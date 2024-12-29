package com.sketch2fashion.backend.service.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GallerysResponseDto {

    private List<GalleryListResponseDto> galleryListResponseDtos;

    public static GallerysResponseDto from(final List<GalleryListResponseDto> galleryListResponseDtos) {
        return new GallerysResponseDto(galleryListResponseDtos);
    }
}
