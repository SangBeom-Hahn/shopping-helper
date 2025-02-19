package com.sketch2fashion.backend.domain.history;

import java.util.Arrays;

public enum MethodName {

    UPLOAD("upload")
    ;

    private final String name;

    MethodName(String name) {
        this.name = name;
    }

    public static boolean isUpload(String name) {
        return from(name) == UPLOAD;
    }

    private static MethodName from(String name) {
        return Arrays.stream(values())
                .filter(methodName -> methodName.name.equals(name))
                .findFirst()
                .orElse(null);
    }
}
