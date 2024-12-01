package com.sketch2fashion.backend.exception;

import com.google.api.gax.rpc.HeaderProvider;
import org.springframework.http.HttpStatus;

public class InferenceFailException extends HelperException {
    public InferenceFailException() {
        super(
                "검색에 실패했습니다. 신속히 조치하겠습니다.",
                "검색에 실패했습니다. 신속히 조치하겠습니다.",
                HttpStatus.INTERNAL_SERVER_ERROR,
                "5002"
        );
    }
}
