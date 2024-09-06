package com.sketch2fashion.backend.domain.modelresult;

public enum Status {

    WAIT("현재, 채색 요청이 많아 지체되고 있습니다. 잠시만 기다려주세요."),
    FINISH("채색이 완료되었습니다. 결과를 확인해주세요."),
    ERROR("에러가 발생했습니다. 원인을 분석 중이니 잠시만 기다려주세요.")
    ;

    private final String message;

    Status(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
