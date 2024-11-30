package com.sketch2fashion.backend.exception;

import org.springframework.http.HttpStatus;

public class HealthCheckException extends HelperException {
    public HealthCheckException() {
        super(
                "추론 서버가 가동 중이지 않습니다. 빠르게 조치하겠습니다.",
                "추론 서버가 가동 중이지 않습니다. 빠르게 조치하겠습니다.",
                HttpStatus.INTERNAL_SERVER_ERROR,
                "5001"
        );
    }
}
