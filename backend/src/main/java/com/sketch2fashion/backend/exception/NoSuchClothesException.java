package com.sketch2fashion.backend.exception;

import org.springframework.http.HttpStatus;

public class NoSuchClothesException extends HelperException {

    public NoSuchClothesException(final Long id) {
        super(
                String.format("존재하지 않는 메세지로 요청한 의류 정보입니다. id = {%d}", id),
                "존재하지 않는 메세지로 요청한 의류 정보입니다.",
                HttpStatus.NOT_FOUND,
                "4002"
        );
    }
}
