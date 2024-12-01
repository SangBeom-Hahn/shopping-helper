package com.sketch2fashion.backend.exception;

import com.google.api.Http;
import org.springframework.http.HttpStatus;

public class NoSuchMessageException extends HelperException {

    public NoSuchMessageException(Long id) {
        super(
                String.format("존재하지 않는 메세지 id 입니다. id = {%d}", id),
                "존재하지 않는 메세지 id 입니다.",
                HttpStatus.BAD_REQUEST,
                "4001"
        );
    }
}
