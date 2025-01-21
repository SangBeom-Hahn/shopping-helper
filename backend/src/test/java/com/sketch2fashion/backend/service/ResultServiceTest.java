package com.sketch2fashion.backend.service;

import com.sketch2fashion.backend.controller.dto.ClothesUpdateRequest;
import com.sketch2fashion.backend.domain.message.Message;
import com.sketch2fashion.backend.domain.message.ObjectType;
import com.sketch2fashion.backend.domain.modelresult.ClothesResult;
import com.sketch2fashion.backend.domain.modelresult.Search;
import com.sketch2fashion.backend.domain.upload.Clothes;
import com.sketch2fashion.backend.exception.DuplicateResultException;
import com.sketch2fashion.backend.service.dto.MessageResponseDto;
import com.sketch2fashion.backend.service.dto.ResultResponseDto;
import com.sketch2fashion.backend.support.consume.dto.InferenceListResponse;
import com.sketch2fashion.backend.support.consume.dto.InferencesResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class ResultServiceTest extends ServiceTest {

    private Message message;
    private Message message1;
    private Message message2;
    private ClothesResult clothesResult;
    private ClothesResult clothesResult1;
    private Search search;
    private Clothes clothes;

    @BeforeEach
    void setUp() {
        databaseCleaner.execute();

        message = new Message(ObjectType.SKIRT, "path", false);
        message1 = new Message(ObjectType.SKIRT, "path", false);
        message2 = new Message(ObjectType.SKIRT, "path", false);
        clothes = new Clothes(message, "name", "path");
        clothesResult = new ClothesResult(message);
        clothesResult1 = new ClothesResult(message1);
        search = new Search("url", "url", "url", "name", "site", clothesResult);

        messageRepository.save(message);
        messageRepository.save(message1);
        messageRepository.save(message2);
        resultRepository.save(clothesResult);
        resultRepository.save(clothesResult1);
        searchRepository.save(search);
        clothesRepository.save(clothes);
    }

    @Test
    @DisplayName("이미 존재하는 검색 결과를 저장하면 예외가 발생한다.")
    void throwException_duplicateResult() {
        assertThatThrownBy(() -> resultService.handlePersistEntity(message.getId()))
                .isInstanceOf(DuplicateResultException.class)
                .hasMessage(String.format("이미 존재하는 추론 결과 입니다. 결과 ID = {%d}", message.getId()));
    }

    @Test
    @DisplayName("추론 결과가 캐시에 있으면 캐시에서 결과를 읽는다.")
    void saveAndFindResult() {
        // given
        ResultResponseDto resultResponseDto = createResultResponseDto();

        // when
        redisTemplate.opsForValue()
                .set("SEARCH_RESULT_CACHE::" + message.getId(), resultResponseDto);
        InferencesResponse findInferencesResponse = resultService.findResult(message.getId())
                .getInferencesResponse();

        // then
        assertThat(findInferencesResponse).usingRecursiveComparison()
                .isEqualTo(resultResponseDto.getInferencesResponse());
    }

    @Test
    @DisplayName("검색 화면을 나가면, 검색 결과를 DB에 저장하고, 캐시를 삭제한다.")
    void handlePersistEntity() {
        // given
        ResultResponseDto resultResponseDto = createResultResponseDto();
        redisTemplate.opsForValue()
                .set("SEARCH_RESULT_CACHE::" + message1.getId(), resultResponseDto);

        // when
        resultService.handlePersistEntity(message1.getId());
        List<Search> searches = searchRepository.findAllByClothes(clothesResult1);

        // then
        assertAll(
                () -> assertThat(searches.get(0)).extracting("thumbnailUrl", "webSearchUrl", "hostPageUrl", "site")
                        .containsExactly(
                                resultResponseDto.getInferencesResponse().getResult().get(0).getThumbnailUrl(),
                                resultResponseDto.getInferencesResponse().getResult().get(0).getWebSearchUrl(),
                                resultResponseDto.getInferencesResponse().getResult().get(0).getHostPageUrl(),
                                resultResponseDto.getInferencesResponse().getResult().get(0).getSite()
                        ),
                () -> assertThat(searches.get(1)).extracting("thumbnailUrl", "webSearchUrl", "hostPageUrl", "site")
                        .containsExactly(
                                resultResponseDto.getInferencesResponse().getResult().get(1).getThumbnailUrl(),
                                resultResponseDto.getInferencesResponse().getResult().get(1).getWebSearchUrl(),
                                resultResponseDto.getInferencesResponse().getResult().get(1).getHostPageUrl(),
                                resultResponseDto.getInferencesResponse().getResult().get(1).getSite()
                        ),
                () -> assertThat(searches.get(2)).extracting("thumbnailUrl", "webSearchUrl", "hostPageUrl", "site")
                        .containsExactly(
                                resultResponseDto.getInferencesResponse().getResult().get(2).getThumbnailUrl(),
                                resultResponseDto.getInferencesResponse().getResult().get(2).getWebSearchUrl(),
                                resultResponseDto.getInferencesResponse().getResult().get(2).getHostPageUrl(),
                                resultResponseDto.getInferencesResponse().getResult().get(2).getSite()
                        ),
                () -> assertThat(resultService.findResult(message1.getId())).isNull()
        );
    }

    @Test
    @DisplayName("채색 결과에 평점을 부여한다.")
    void updateResult() throws IOException {
        // given
        FileInputStream fileInputStream = new FileInputStream(new File("src/test/resources/test.jpeg"));
        MockMultipartFile file = new MockMultipartFile("mockfile", "test", "jpeg", fileInputStream);
        String URL = "https://storage.googleapis.com/test/1234abcd1234abcd.png";
        int expectedRate = 5;
        String expectedReview = "GOOD";
        ClothesUpdateRequest clothesUpdateRequest = new ClothesUpdateRequest(expectedRate, expectedReview);

        // when
        Long saveId = clothesService.createClothes(message2.getId(), URL, file.getName())
                .getId();

        resultService.updateResult(saveId, clothesUpdateRequest.getRating(), clothesUpdateRequest.getReview());
        ClothesResult findClothesResult = resultRepository.findById(saveId)
                .orElseThrow();

        // then
        assertThat(findClothesResult).extracting("rating", "review")
                .containsExactly(expectedRate, expectedReview);
    }

    private ResultResponseDto createResultResponseDto() {
        List<InferenceListResponse> inferenceListResponses = Stream.generate(() -> InferenceListResponse.of("url", "url", "url", "name","site"))
                .limit(3)
                .collect(Collectors.toList());
        InferencesResponse inferencesResponse = InferencesResponse.of(1, 1, 1, 1, 1, 1, 1, inferenceListResponses);
        return ResultResponseDto.of(inferencesResponse, "path", "path");
    }
}