package com.sketch2fashion.backend.domain.file;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Getter
public enum FileExtension {
    JPEG(".jpeg"),
    JPG(".jpg"),
    PNG(".png"),
    HEIC(".heic")
    ;

    private final String values;

    FileExtension(String values) {
        this.values = values;
    }

    public static FileExtension from(String fileName) {
        return Arrays.stream(values())
                .filter(fileExtension -> fileExtension.values.equals(getExtension(fileName)))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException());
    }

    public static String getExtension(String fileName) {
        return fileName.substring(getDotIndex(fileName));
    }

    private static int getDotIndex(String fileName) {
        return fileName.lastIndexOf(".");
    }

    public static boolean isValidFormat(String fileName) {
        return Arrays.stream(values())
                .anyMatch(fileExtension -> fileExtension.values.equals(getExtension(fileName)));
    }
}
