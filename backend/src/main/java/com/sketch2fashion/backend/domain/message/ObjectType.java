package com.sketch2fashion.backend.domain.message;

public enum ObjectType {

    CLOTHES("CLOTHES");

    private final String value;

    ObjectType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
