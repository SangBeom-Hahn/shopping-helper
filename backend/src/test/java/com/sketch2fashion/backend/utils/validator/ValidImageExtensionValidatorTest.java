package com.sketch2fashion.backend.utils.validator;

import jakarta.validation.ConstraintValidatorContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ValidImageExtensionValidatorTest {


    private ValidImageExtensionValidator validator;

    private ConstraintValidatorContext context;

    @BeforeEach
    public void setUp() {
        validator = new ValidImageExtensionValidator();
        context = mock(ConstraintValidatorContext.class);
    }

    @Test
    @DisplayName("지원하지 않는 포멧을 입력하면 validator를 통과하지 못한다.")
    void notAllowedExtension() throws IOException {
        // given
        InputStream rawFile = new FileInputStream(
                "src/test/resources/test.txt"
        );
        MockMultipartFile fakeFile = new MockMultipartFile("test", "test.txt", "txt", rawFile);

        // then
        Assertions.assertThat(validator.isValid(fakeFile, context))
                .isFalse();
    }

    @Test
    @DisplayName("빈 파일이 아닌 파일 업로드 검증을 통과한다.")
    void validFileExtension() throws IOException {
        // given
        InputStream rawFile = new FileInputStream(
                "src/test/resources/test.jpeg"
        );
        MockMultipartFile fakeFile = new MockMultipartFile("test", "test.jpeg", "jpeg", rawFile);

        // then
        assertThat(validator.isValid(fakeFile, context))
                .isTrue();
    }
}