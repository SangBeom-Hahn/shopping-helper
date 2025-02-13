package com.sketch2fashion.backend.acceptance;

import com.sketch2fashion.backend.controller.dto.ClothesSaveRequest;
import com.sketch2fashion.backend.controller.dto.ClothesSharedRequest;
import com.sketch2fashion.backend.controller.dto.ClothesUpdateRequest;
import com.sketch2fashion.backend.domain.file.FileMetaData;
import com.sketch2fashion.backend.service.dto.ClothesSaveResponseDto;
import com.sketch2fashion.backend.service.dto.GallerysResponseDto;
import com.sketch2fashion.backend.service.dto.MessageResponseDto;
import com.sketch2fashion.backend.service.dto.SearchsResponseDto;
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
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

class GalleryAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("갤러리를 조회한다.")
    void findGallery() throws IOException {
        // given
        given(fakeUploader.upload(any(FileMetaData.class)))
                .willReturn(testUuidHolder.createUuid());
        willDoNothing().given(fakePublisher)
                .sendModelMessage(any(MessageResponseDto.class));
        ClothesSaveRequest clothesSaveRequest = createClothesSaveRequest();
        Long messageId1 = 업로드(clothesSaveRequest).as(ClothesSaveResponseDto.class)
                .getId();
        Long messageId2 = 업로드(clothesSaveRequest).as(ClothesSaveResponseDto.class)
                .getId();

        ClothesUpdateRequest clothesUpdateRequest = new ClothesUpdateRequest(5, "REVIEW");
        평점_부여(messageId1, clothesUpdateRequest);
        평점_부여(messageId2, clothesUpdateRequest);

        ClothesSharedRequest clothesSharedRequest = new ClothesSharedRequest(true);
        추론_결과_공유(messageId1, clothesSharedRequest);
        추론_결과_공유(messageId2, clothesSharedRequest);

        // when
        ExtractableResponse<Response> response = 갤러리_조회();
        GallerysResponseDto gallerysResponseDto = response.as(GallerysResponseDto.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(gallerysResponseDto.getGalleryListResponseDtos()).hasSize(2),
                () -> assertThat(gallerysResponseDto.getGalleryListResponseDtos()).extracting("uploadFileName", "rating")
                        .containsExactlyInAnyOrder(
                                tuple(clothesSaveRequest.getImage().getOriginalFilename(), clothesUpdateRequest.getRating()),
                                tuple(clothesSaveRequest.getImage().getOriginalFilename(), clothesUpdateRequest.getRating())
                        )
        );
    }

    @Test
    @DisplayName("갤러리에 공유된 검색 결과를 조회한다.")
    void findSearchs() throws IOException {
        // given
        given(fakeUploader.upload(any(FileMetaData.class)))
                .willReturn(testUuidHolder.createUuid());
        willDoNothing().given(fakePublisher)
                .sendModelMessage(any(MessageResponseDto.class));
        ClothesSaveRequest clothesSaveRequest = createClothesSaveRequest();
        Long messageId = 업로드(clothesSaveRequest).as(ClothesSaveResponseDto.class)
                .getId();

        ClothesSharedRequest clothesSharedRequest = new ClothesSharedRequest(true);
        추론_결과_공유(messageId, clothesSharedRequest);

        List<InferenceListResponse> inferenceListResponses = List.of(
                InferenceListResponse.of("URL", "URL", "URL", "name", "site"),
                InferenceListResponse.of("URL", "URL", "URL", "name", "site"),
                InferenceListResponse.of("URL", "URL", "URL", "name", "site"),
                InferenceListResponse.of("URL", "URL", "URL", "name", "site")
        );
        InferencesResponse inferencesResponse =
                InferencesResponse.of(0, 0, 0, 0, 0, 0, 0, inferenceListResponses);

        given(signedUrlBuilder.generateSignedUrl(any()))
                .willReturn(testUuidHolder.createUuid());
        resultService.saveResult(messageId, inferencesResponse);

        // when
        검색_결과_저장(messageId);
        SearchsResponseDto searchsResponseDto = 검색_결과_조회(messageId).as(SearchsResponseDto.class);

        // then
        assertAll(
                () -> assertThat(searchsResponseDto.getSearchResponseDtos()).hasSize(4)
        );
    }
}