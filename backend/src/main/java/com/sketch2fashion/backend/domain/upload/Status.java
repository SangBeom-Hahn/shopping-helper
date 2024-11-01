package com.sketch2fashion.backend.domain.upload;

public enum Status {

    SUCCESS("업로드가 완료되었습니다. 검색이 완료되면 자동으로 화면이 전환됩니다. 잠시만 기다려주세요."),
    FAIL("업로드에 실패했습니다. 파일을 확인 후 다시 업로드 해주시길 바랍니다.")
    ;

    private final String message;

    Status(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
