package com.sketch2fashion.backend.domain.file;

import java.util.Arrays;

public enum BasicSketch {

    TSHIRTS1("TSHIRTS1", "티셔츠1.PNG"),
    TSHIRTS2("TSHIRTS2", "티셔츠2.PNG"),
    PANTS1("PANTS1", "바지1.PNG"),
    PANTS2("PANTS2", "바지2.PNG"),
    HAT1("HAT1", "모자1.PNG"),
    HAT2("HAT2", "모자2.PNG"),
    SKIRT1("SKIRT1", "치마1.PNG"),
    SKIRT2("SKIRT2", "치마2.PNG"),
    ;

    private final String type;
    private final String name;

    BasicSketch(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public static BasicSketch from(final String type) {
        return Arrays.stream(values())
                .filter(basic -> basic.type.equals(type))
                .findFirst()
                .orElseThrow();
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
