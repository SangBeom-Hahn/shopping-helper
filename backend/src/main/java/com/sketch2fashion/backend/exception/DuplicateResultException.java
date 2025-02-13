package com.sketch2fashion.backend.exception;

import org.springframework.http.HttpStatus;

public class DuplicateResultException extends HelperException {
    public DuplicateResultException(final Long id) {
        super(
                String.format("이미 존재하는 추론 결과 입니다. 결과 ID = {%d}", id),
                "이미 존재하는 추론 결과 입니다.",
                HttpStatus.BAD_REQUEST,
                "4006"
        );
    }
}
