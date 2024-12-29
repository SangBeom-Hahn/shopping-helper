package com.sketch2fashion.backend.exception;

import org.springframework.http.HttpStatus;

public class NoSuchTypeException extends HelperException {

    public NoSuchTypeException(final String type) {
        super(
                String.format("존재하지 않는 TYPE 입니다. type = {%s}", type),
                "존재하지 않는 TYPE 입니다.",
                HttpStatus.BAD_REQUEST,
                "4005"
        );
    }
}
