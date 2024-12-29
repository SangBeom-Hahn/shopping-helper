package com.sketch2fashion.backend.service.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClothesSaveResponseDto {

    private Long id;

    public static ClothesSaveResponseDto from(final Long id) {
        return new ClothesSaveResponseDto(id);
    }
}
