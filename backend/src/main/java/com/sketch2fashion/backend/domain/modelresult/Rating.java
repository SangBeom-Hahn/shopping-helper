package com.sketch2fashion.backend.domain.modelresult;

public enum Rating {

    FIRST("5"),
    SECOND("4"),
    THIRD("3"),
    FIRTH("2"),
    FIFTH("1")
    ;

    private final String value;

    Rating(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
