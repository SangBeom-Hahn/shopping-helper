package com.sketch2fashion.backend.controller.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.sketch2fashion.backend.controller.dto.ValidateMessage.EMPTY_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;

class ClothesSaveRequestTest extends RequestTest {

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("카테고리로 빈 문자열이 들어오면 처리한다.")
    void blankType(String objectType) throws IOException {
        // given
        String originalFileName = "test.jpeg";
        InputStream fileInputStream = new FileInputStream("src/test/resources/" + originalFileName);
        MockMultipartFile mockFile = new MockMultipartFile("test_mock", originalFileName, "jpeg", fileInputStream);
        ClothesSaveRequest clothesSaveRequest = new ClothesSaveRequest(
                objectType,
                false,
                mockFile
        );

        // then
        assertThat(isEmpty(clothesSaveRequest)).isTrue();
    }
    
    @ParameterizedTest
    @NullSource
    @DisplayName("정제 여부로 Null이 들어오면 처리한다.")
    void nullRefine(Boolean refine) throws IOException {
        // given  
        String originalFileName = "test.jpeg";
        InputStream fileInputStream = new FileInputStream("src/test/resources/" + originalFileName);
        MockMultipartFile mockFile = new MockMultipartFile("test_mock", originalFileName, "jpeg", fileInputStream);
        ClothesSaveRequest clothesSaveRequest = new ClothesSaveRequest(
                "HAT",
                refine,
                mockFile
        );

        // then
        assertThat(isEmpty(clothesSaveRequest)).isTrue();
    }

    private boolean isEmpty(ClothesSaveRequest clothesSaveRequest) {
        return getConstraintViolation(clothesSaveRequest).stream()
                .anyMatch(violation -> violation.getMessage().equals(EMPTY_MESSAGE));
    }
}