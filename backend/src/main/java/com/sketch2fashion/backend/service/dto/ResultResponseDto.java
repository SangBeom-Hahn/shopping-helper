package com.sketch2fashion.backend.service.dto;

import com.sketch2fashion.backend.support.consume.dto.InferencesResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResultResponseDto {

    private InferencesResponse inferencesResponse;

    private String uploadFilePath;

    private String resultFilePath;

    public static ResultResponseDto of(
            final InferencesResponse inferencesResponse,
            final String uploadFilePath,
            final String resultFilePath
    ) {
        return new ResultResponseDto(inferencesResponse, uploadFilePath, resultFilePath);
    }
}
