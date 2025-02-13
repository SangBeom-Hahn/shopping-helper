package com.sketch2fashion.backend.support.consume;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum StatusCode {

    OK(200),
    BAD(500)
    ;

    private int value;

    StatusCode(int value) {
        this.value = value;
    }

    public static StatusCode from(final int value) {
        return Arrays.stream(values())
                .filter(statusCode -> statusCode.value == value)
                .findFirst()
                .orElseThrow();
    }

    public boolean isOk() {
        return this == OK;
    }
}
