package com.sketch2fashion.backend.domain.modelresult;

public enum Rating {

    FIRST(5),
    SECOND(4),
    THIRD(3),
    FIRTH(2),
    FIFTH(1)
    ;

    private final int value;

    Rating(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
