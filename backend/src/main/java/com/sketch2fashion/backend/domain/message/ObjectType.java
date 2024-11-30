package com.sketch2fashion.backend.domain.message;

import com.sketch2fashion.backend.exception.NoSuchTypeException;

import java.util.Arrays;

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

    public static ObjectType from(String objectType) {
        return Arrays.stream(values())
                .filter(type -> type.value.equals(objectType))
                .findFirst()
                .orElseThrow(() -> new NoSuchTypeException(objectType));
    }

    public static boolean isTshirts(ObjectType objectType) {
        return objectType == ObjectType.T_SHIRTS;
    }

    public static boolean isPants(ObjectType objectType) {
        return objectType == ObjectType.PANTS;
    }

    public static boolean isHat(ObjectType objectType) {
        return objectType == ObjectType.HAT;
    }

    public String getValue() {
        return value;
    }
}
