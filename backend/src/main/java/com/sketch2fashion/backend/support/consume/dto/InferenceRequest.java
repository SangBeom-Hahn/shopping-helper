package com.sketch2fashion.backend.support.consume.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InferenceRequest {

    private String storeFilePath;

    public static InferenceRequest from(String storeFilePath) {
        return new InferenceRequest(storeFilePath);
    }
}
