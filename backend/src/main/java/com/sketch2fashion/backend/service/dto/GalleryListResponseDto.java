package com.sketch2fashion.backend.service.dto;

import com.sketch2fashion.backend.domain.modelresult.ClothesResult;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GalleryListResponseDto {

    private Long id;

    private String storeFilePath;

    private String uploadFileName;

    public static GalleryListResponseDto of(ClothesResult clothesResult, String uploadFileName) {
        return new GalleryListResponseDto(
                clothesResult.getId(),
                clothesResult.getStoreFilePath(),
                uploadFileName
        );
    }
}
