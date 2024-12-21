package com.sketch2fashion.backend.repository;

import com.sketch2fashion.backend.domain.message.Message;
import com.sketch2fashion.backend.domain.message.ObjectType;
import com.sketch2fashion.backend.domain.modelresult.ClothesResult;
import com.sketch2fashion.backend.domain.modelresult.Search;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SearchRepositoryTest extends RepositoryTest {

    private Message message;
    private ClothesResult clothes;
    private Search search;
    private Search search1;

    @BeforeEach
    void setUp() {
        message = new Message(ObjectType.SKIRT, "path", false);
        clothes = new ClothesResult(message);
        search = new Search("url", "url", "url", "site", "name", clothes);
        search1 = new Search("url", "url", "url", "site", "name", clothes);

        messageRepository.save(message);
        resultRepository.save(clothes);
    }

    @Test
    @DisplayName("Bing Image Search API 결과를 저장한다.")
    void save() {
        // given
        Search actual = searchRepository.save(search);

        // then
        assertAll(
                () -> assertThat(search.getId()).isNotNull(),
                () -> assertThat(actual).isEqualTo(search)
        );
    }

    @Test
    @DisplayName("Bing Image Search API 결과를 조회한다.")
    void findById() {
        // given
        Long saveId = searchRepository.save(search)
                .getId();

        // when
        Search findSearch = searchRepository.findById(saveId)
                .orElseThrow();
        ClothesResult findClothes = findSearch.getClothes();
        Message findMessage = findClothes.getMessage();

        // then
        assertAll(
                () -> assertThat(findClothes).isEqualTo(clothes),
                () -> assertThat(findMessage).isEqualTo(message),
                () -> assertThat(findSearch).usingRecursiveComparison()
                        .isEqualTo(search)
        );
    }

    @Test
    @DisplayName("채색 결과물로 검색 결과물을 조회한다.")
    void findAllByClothes() {
        // given
        searchRepository.save(search);
        searchRepository.save(search1);
        List<Search> expected = List.of(search, search1);

        // when
        List<Search> actual = searchRepository.findAllByClothes(clothes);

        // then
        assertAll(
                () -> assertThat(actual).hasSize(2),
                () -> assertThat(actual).usingRecursiveAssertion()
                        .isEqualTo(expected)
        );
    }
}