package com.sketch2fashion.backend.exception;

import org.springframework.http.HttpStatus;

public class InvalidFileFormatException extends HelperException {

    public InvalidFileFormatException() {
        super(
                "[jpg, jpeg, png, heic] 형태만 업로드 가능합니다.",
                "[jpg, jpeg, png, heic] 형태만 업로드 가능합니다.",
                HttpStatus.BAD_REQUEST,
                "4004"
        );
    }
}
