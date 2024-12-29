package com.sketch2fashion.backend.domain.file;

import com.sketch2fashion.backend.exception.InvalidFileFormatException;
import lombok.Getter;

import java.util.Arrays;

import static com.sketch2fashion.backend.utils.SketchConstants.IDX_STAND;

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

    public static FileExtension from(final String fileName) {
        return Arrays.stream(values())
                .filter(fileExtension -> fileExtension.values.equals(getExtension(fileName)))
                .findFirst()
                .orElseThrow(() -> new InvalidFileFormatException());
    }

    public static String getExtension(final String fileName) {
        return fileName.substring(getDotIndex(fileName));
    }

    private static int getDotIndex(final String fileName) {
        return fileName.lastIndexOf(IDX_STAND);
    }

    public static boolean isValidFormat(final String fileName) {
        return Arrays.stream(values())
                .anyMatch(fileExtension -> fileExtension.values.equals(getExtension(fileName)));
    }
}
