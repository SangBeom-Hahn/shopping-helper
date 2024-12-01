package com.sketch2fashion.backend.exception;

import jakarta.persistence.GeneratedValue;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class HelperException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String showMessage;
    private final String errorCode;

    public HelperException(
            String message,
            String showMessage,
            HttpStatus httpStatus,
            String errorCode
    ) {
        super(message);
        this.httpStatus = httpStatus;
        this.showMessage = showMessage;
        this.errorCode = errorCode;
    }
}
