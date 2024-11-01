package com.sketch2fashion.backend.domain.message;

public enum ObjectType {

    T_SHIRTS("T_SHIRTS"),
    PANTS("PANTS"),
    SKIRT("SKIRT"),
    HAT("HAT")
    ;

    private final String value;

    ObjectType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
