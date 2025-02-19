package com.sketch2fashion.backend.support.consume.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private String error;

    public static ErrorResponse from(String message) {
        return new ErrorResponse(message);
    }
}