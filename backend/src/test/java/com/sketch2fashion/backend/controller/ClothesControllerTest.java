package com.sketch2fashion.backend.controller;

import com.sketch2fashion.backend.controller.dto.ClothesSaveRequest;
import com.sketch2fashion.backend.domain.file.FileMetaData;
import com.sketch2fashion.backend.service.dto.ClothesSaveResponseDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class ClothesControllerTest extends AcceptanceTest {

    @Test
    @DisplayName("업로드 API로 업로드에 성공하면 202로 응답한다.")
    void upload() throws IOException {
        // given
        String URL = "src/test/resources/1234abcd1234abcd.jpeg";
        String originalFileName = "test.jpeg";
        InputStream fileInputStream = new FileInputStream("src/test/resources/" + originalFileName);
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "test.jpeg",
                "image/jpeg",
                fileInputStream
        );
        when(fakeUploader.upload(any(FileMetaData.class)))
                .thenReturn(URL);
        doNothing().when(fakePublisher).sendModelMessage(any());
        ClothesSaveRequest clothesSaveRequest = new ClothesSaveRequest("HAT", false, image);

        // when
        ExtractableResponse<Response> response = uploadForm(clothesSaveRequest);
        ClothesSaveResponseDto clothesSaveResponseDto = response.as(ClothesSaveResponseDto.class);

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(clothesSaveResponseDto.getId()).isEqualTo(1L)
        );
    }

    private ExtractableResponse<Response> uploadForm(ClothesSaveRequest clothesSaveRequest) throws IOException {
        MultipartFile image = clothesSaveRequest.getImage();

        return RestAssured
                .given()
                .accept("application/json")
                .contentType(ContentType.MULTIPART)
                .multiPart("image",
                        image.getOriginalFilename(),
                        image.getInputStream(),
                        image.getContentType()
                )
                .multiPart("objectType", clothesSaveRequest.getObjectType())
                .multiPart("refine", clothesSaveRequest.getRefine())
                .when().post("/api/clothes/upload")
                .then().log().all().extract();
    }
}