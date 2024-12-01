package com.sketch2fashion.backend.service;

import com.sketch2fashion.backend.domain.message.Message;
import com.sketch2fashion.backend.domain.message.ObjectType;
import com.sketch2fashion.backend.domain.modelresult.ClothesResult;
import com.sketch2fashion.backend.domain.modelresult.Search;
import com.sketch2fashion.backend.domain.upload.Clothes;
import com.sketch2fashion.backend.exception.DuplicateResultException;
import com.sketch2fashion.backend.service.dto.ResultResponseDto;
import com.sketch2fashion.backend.support.consume.dto.InferenceListResponse;
import com.sketch2fashion.backend.support.consume.dto.InferencesResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

class ResultServiceTest extends ServiceTest {

    private Message message;
    private Message message1;
    private ClothesResult clothesResult;
    private ClothesResult clothesResult1;
    private Search search;
    private Clothes clothes;

    @BeforeEach
    void setUp() {
        message = new Message(ObjectType.SKIRT, "path", false);
        message1 = new Message(ObjectType.SKIRT, "path", false);
        clothes = new Clothes(message, "name", "path");
        clothesResult = new ClothesResult(message);
        clothesResult1 = new ClothesResult(message1);
        search = new Search("url", "url", "url", "site", clothesResult);

        messageRepository.save(message);
        messageRepository.save(message1);
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
                .set("RESULT_CACHE::" + message.getId(), resultResponseDto);
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
                .set("RESULT_CACHE::" + message1.getId(), resultResponseDto);

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

    private ResultResponseDto createResultResponseDto() {
        List<InferenceListResponse> inferenceListResponses = Stream.generate(() -> InferenceListResponse.of("url", "url", "url", "site"))
                .limit(3)
                .collect(Collectors.toList());
        InferencesResponse inferencesResponse = InferencesResponse.of(1, 1, 1, 1, 1, 1, 1, inferenceListResponses);
        return ResultResponseDto.of(inferencesResponse, "path", "path");
    }
}