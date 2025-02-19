package com.sketch2fashion.backend.support.consume;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum StatusCode {

    OK(200),
    BAD_GATE_Way(502)
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

    public boolean isBadGateWay() {
        return this == BAD_GATE_Way;
    }
}
