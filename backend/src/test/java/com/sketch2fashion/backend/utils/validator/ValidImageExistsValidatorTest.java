package com.sketch2fashion.backend.utils.validator;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class ValidImageExistsValidatorTest {

    private ValidImageExistsValidator validator;

    private ConstraintValidatorContext context;

    @BeforeEach
    public void setUp() {
        validator = new ValidImageExistsValidator();
        context = mock(ConstraintValidatorContext.class);
    }

    @Test
    @DisplayName("빈 파일을 업로드하면 validator를 통과하지 못한다.")
    void notAllowedAbsentImage() {
        // given
        MockMultipartFile fakeFile = null;

        // when
        boolean actual = validator.isValid(null, context);

        // then
        assertThat(actual).isFalse();
    }
    
    @Test
    @DisplayName("빈 파일이 아닌 파일 업로드 검증을 통과한다.")
    void validFileExists() throws IOException {
        // given
        InputStream rawFile = new FileInputStream(
                "src/test/resources/test.jpeg"
        );
        MockMultipartFile fakeFile = new MockMultipartFile(
                "test",
                rawFile
        );
      
        // then
        assertThat(validator.isValid(fakeFile, context))
                .isTrue();
    }
}