package com.sketch2fashion.backend.acceptance;

import com.sketch2fashion.backend.controller.dto.ClothesSaveRequest;
import com.sketch2fashion.backend.controller.dto.ClothesSharedRequest;
import com.sketch2fashion.backend.controller.dto.ClothesUpdateRequest;
import com.sketch2fashion.backend.domain.file.FileMetaData;
import com.sketch2fashion.backend.domain.message.Message;
import com.sketch2fashion.backend.domain.message.ObjectType;
import com.sketch2fashion.backend.domain.modelresult.ClothesResult;
import com.sketch2fashion.backend.service.dto.ClothesSaveResponseDto;
import com.sketch2fashion.backend.service.dto.MessageResponseDto;
import com.sketch2fashion.backend.service.dto.ResultResponseDto;
import com.sketch2fashion.backend.support.consume.dto.InferenceListResponse;
import com.sketch2fashion.backend.support.consume.dto.InferencesResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
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
        String originalFileName = "test.jpeg";
        InputStream fileInputStream = new FileInputStream("src/test/resources/" + originalFileName);
        MockMultipartFile image = new MockMultipartFile(
                "image",
                originalFileName,
                "image/jpeg",
                fileInputStream
        );
        given(fakeUploader.upload(any(FileMetaData.class)))
                        .willReturn("PATH");
        willDoNothing().given(fakePublisher)
                .sendModelMessage(any(MessageResponseDto.class));

        ClothesSaveRequest clothesSaveRequest = new ClothesSaveRequest("HAT", false, image);

        // when
        ExtractableResponse<Response> response = upload(clothesSaveRequest);
        ClothesSaveResponseDto clothesSaveResponseDto = response.as(ClothesSaveResponseDto.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(clothesSaveResponseDto.getId()).isEqualTo(1L)
        );
    }

    /** TODO: 이제는 서비스나 레포를 쓰는게 아니라 컨트롤러를 여러개 써서 테스트를 작성해야 한다.
     * 업로드를 하고 (API 호출)
     * 리스너에서 resultService.saveResult로 결과를 큐에 저장하고
     * findInfer API 호출
     */

    @Test
    @DisplayName("추론 결과를 조회한다.")
    void findInferenceResult() {
        // given
        Long messageId = 1L;
        String uploadFilePath = "uploadFilePath";
        String resultFilePath = "resultFilePath";
        List<InferenceListResponse> inferenceListResponses = List.of(
                InferenceListResponse.of("URL", "URL", "URL", "name", "site"),
                InferenceListResponse.of("URL", "URL", "URL", "name", "site"),
                InferenceListResponse.of("URL", "URL", "URL", "name", "site"),
                InferenceListResponse.of("URL", "URL", "URL", "name", "site")
        );
        InferencesResponse inferencesResponse =
                InferencesResponse.of(0, 0, 0, 0, 0, 0, 0, inferenceListResponses);
        ResultResponseDto result = ResultResponseDto.of(inferencesResponse, uploadFilePath, resultFilePath);
        redisTemplate.opsForValue()
                .set("RESULT_CACHE::" + messageId, result);

        // when
        ExtractableResponse<Response> response = findInferenceResult(messageId);
        ResultResponseDto actual = response.as(ResultResponseDto.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(actual).usingRecursiveComparison()
                        .isEqualTo(result)
        );
    }

    @Test
    @DisplayName("검색 결과 평점을 부여한다.")
    void updateRate() {
        // given
        Long messageId = 1L;
        ClothesUpdateRequest clothesUpdateRequest = new ClothesUpdateRequest(5, "REVIEW");

        Message message = new Message(ObjectType.T_SHIRTS, "PATH", false);
        messageRepository.save(message);
        ClothesResult clothesResult = new ClothesResult(message);
        resultRepository.save(clothesResult);

        // when
        ExtractableResponse<Response> response = updateRate(messageId, clothesUpdateRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("추론 결과를 갤러리에 공유한다.")
    void share() {
        // given
        Long messageId = 1L;
        ClothesSharedRequest clothesSharedRequest = new ClothesSharedRequest(true);
        Message message = new Message(ObjectType.T_SHIRTS, "PATH", false);
        messageRepository.save(message);
        ClothesResult clothesResult = new ClothesResult(message);
        resultRepository.save(clothesResult);

        // when
        ExtractableResponse<Response> response = share(messageId, clothesSharedRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> upload(ClothesSaveRequest clothesSaveRequest) throws IOException {
        MultipartFile image = clothesSaveRequest.getImage();

        return RestAssured
                .given()
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .multiPart("image",
                        image.getOriginalFilename(),
                        image.getInputStream(),
                        image.getContentType()
                )
                .param("objectType", clothesSaveRequest.getObjectType())
                .param("refine", clothesSaveRequest.getRefine())
                .when().post("/api/clothes/upload")
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> findInferenceResult(Long messageId) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/clothes/" + messageId)
                .then().log().all().extract();

    }

    private ExtractableResponse<Response> updateRate(Long messageId, ClothesUpdateRequest clothesUpdateRequest) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(clothesUpdateRequest)
                .put("/api/clothes/" + messageId)
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> share(Long messageId, ClothesSharedRequest clothesSharedRequest) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(clothesSharedRequest)
                .put("/api/clothes/" + messageId + "/share")
                .then().log().all().extract();
    }
}