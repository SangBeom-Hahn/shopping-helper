package com.sketch2fashion.backend.support.consume.dto;

import com.sketch2fashion.backend.service.dto.MessageResponseDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InferenceRequest {

    private Long id;

    private String storeFilePath;

    public static InferenceRequest from(MessageResponseDto messageResponseDto) {
        return new InferenceRequest(
                messageResponseDto.getId(),
                messageResponseDto.getStoreFilePath()
        );
    }
}
