package com.sketch2fashion.backend.exception;

import org.springframework.http.HttpStatus;

public class AbsentFileException extends HelperException {

    public AbsentFileException() {
        super(
                "업로드 파일이 Null 값입니다. 다시 업로드하시길 바랍니다.",
                "업로드 파일이 Null 값입니다. 다시 업로드하시길 바랍니다.",
                HttpStatus.BAD_REQUEST,
                "4003"
        );
    }
}
