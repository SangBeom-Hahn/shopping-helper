package com.sketch2fashion.backend.acceptance;

import com.sketch2fashion.backend.controller.dto.ClothesSaveRequest;
import com.sketch2fashion.backend.controller.dto.ClothesSharedRequest;
import com.sketch2fashion.backend.controller.dto.ClothesUpdateRequest;
import com.sketch2fashion.backend.domain.file.FileMetaData;
import com.sketch2fashion.backend.service.dto.ClothesSaveResponseDto;
import com.sketch2fashion.backend.service.dto.MessageResponseDto;
import com.sketch2fashion.backend.service.dto.ResultResponseDto;
import com.sketch2fashion.backend.support.consume.dto.InferenceListResponse;
import com.sketch2fashion.backend.support.consume.dto.InferencesResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.List;

import static com.sketch2fashion.backend.acceptance.AcceptanceFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

class ClothesAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("이미지 단건 업로드를 수행한다.")
    void upload() throws IOException {
        // given
        given(fakeUploader.upload(any(FileMetaData.class)))
                .willReturn(testUuidHolder.createUuid());
        willDoNothing().given(fakePublisher)
                .sendModelMessage(any(MessageResponseDto.class));
        ClothesSaveRequest clothesSaveRequest = createClothesSaveRequest();

        // when
        ExtractableResponse<Response> response = 업로드(clothesSaveRequest);
        ClothesSaveResponseDto clothesSaveResponseDto = response.as(ClothesSaveResponseDto.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(clothesSaveResponseDto.getId()).isEqualTo(1L)
        );
    }

    @Test
    @DisplayName("추론 결과를 조회한다.")
    void findInferenceResult() throws IOException {
        // given
        String uuid = testUuidHolder.createUuid();
        given(fakeUploader.upload(any(FileMetaData.class)))
                .willReturn(uuid);
        willDoNothing().given(fakePublisher)
                .sendModelMessage(any(MessageResponseDto.class));
        ClothesSaveRequest clothesSaveRequest = createClothesSaveRequest();
        Long saveId = 업로드(clothesSaveRequest).as(ClothesSaveResponseDto.class)
                .getId();

        List<InferenceListResponse> inferenceListResponses = List.of(
                InferenceListResponse.of("URL", "URL", "URL", "name", "site"),
                InferenceListResponse.of("URL", "URL", "URL", "name", "site"),
                InferenceListResponse.of("URL", "URL", "URL", "name", "site"),
                InferenceListResponse.of("URL", "URL", "URL", "name", "site")
        );
        InferencesResponse inferencesResponse =
                InferencesResponse.of(0, 0, 0, 0, 0, 0, 0, inferenceListResponses);

        given(signedUrlBuilder.generateSignedUrl(any()))
                .willReturn(uuid);
        resultService.saveResult(saveId, inferencesResponse);
        ResultResponseDto expected =
                ResultResponseDto.of(inferencesResponse, uuid, uuid);

        // when
        ExtractableResponse<Response> response = AcceptanceFixture.추론_결과_조회(saveId);
        ResultResponseDto actual = response.as(ResultResponseDto.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(actual).usingRecursiveComparison()
                        .isEqualTo(expected)
        );
    }

    @Test
    @DisplayName("검색 결과 평점을 부여한다.")
    void updateRate() throws IOException {
        // given
        given(fakeUploader.upload(any(FileMetaData.class)))
                .willReturn(testUuidHolder.createUuid());
        willDoNothing().given(fakePublisher)
                .sendModelMessage(any(MessageResponseDto.class));
        ClothesSaveRequest clothesSaveRequest = createClothesSaveRequest();

        Long messageId = 업로드(clothesSaveRequest).as(ClothesSaveResponseDto.class)
                .getId();

        ClothesUpdateRequest clothesUpdateRequest = new ClothesUpdateRequest(5, "REVIEW");

        // when
        ExtractableResponse<Response> response = 평점_부여(messageId, clothesUpdateRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("추론 결과를 갤러리에 공유한다.")
    void share() throws IOException {
        // given
        given(fakeUploader.upload(any(FileMetaData.class)))
                .willReturn(testUuidHolder.createUuid());
        willDoNothing().given(fakePublisher)
                .sendModelMessage(any(MessageResponseDto.class));
        ClothesSaveRequest clothesSaveRequest = createClothesSaveRequest();

        Long messageId = 업로드(clothesSaveRequest).as(ClothesSaveResponseDto.class)
                .getId();

        ClothesSharedRequest clothesSharedRequest = new ClothesSharedRequest(true);

        // when
        ExtractableResponse<Response> response = 추론_결과_공유(messageId, clothesSharedRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}