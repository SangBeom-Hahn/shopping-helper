package com.sketch2fashion.backend.service.dto;

import com.sketch2fashion.backend.domain.upload.Clothes;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClothesResponseDto {

    private Long id;

    private Long messageId;

    private String uploadFileName;

    private String storeFilePath;

    public static ClothesResponseDto from(Clothes clothes) {
        return new ClothesResponseDto(
                clothes.getId(),
                clothes.getMember(),
                clothes.getUploadFileName(),
                clothes.getStoreFilePath()
        );
    }
}