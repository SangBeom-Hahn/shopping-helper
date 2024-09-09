package com.sketch2fashion.backend.domain.file;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static com.sketch2fashion.backend.domain.file.FileExtension.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class FileExtensionTest {

    @ParameterizedTest
    @DisplayName("지원하지 않는 파일 형식을 업로드하면 예외가 발생한다.")
    @ValueSource(strings = {"a.hwp", "a.word", "a.xlsx", "a.csv", "a.pptx", "a.ppt", "a.mp3", "a.mov", "a.mp4"})
    void throwException_invalidFileExtension(String invalidFileName) {
        assertThatThrownBy(() -> FileExtension.from(invalidFileName))
                .isInstanceOf(IllegalArgumentException.class);
    }
    
    @ParameterizedTest
    @DisplayName("파일에서 파일 확장자를 생성한다.")
    @MethodSource("validFile")
    void construct(String fileName, FileExtension extension) {
        assertThat(FileExtension.from(fileName))
                .isEqualTo(extension);
    }

    private static Stream<Arguments> validFile() {
        return Stream.of(
                Arguments.of("a.jpeg", JPEG),
                Arguments.of("a.jpg", JPG),
                Arguments.of("a.png", PNG),
                Arguments.of("a.heic", HEIC)
        );
    }
}