package com.sketch2fashion.backend.repository;

import com.sketch2fashion.backend.domain.message.Message;
import com.sketch2fashion.backend.domain.message.ObjectType;
import com.sketch2fashion.backend.domain.modelresult.ClothesResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.sketch2fashion.backend.domain.modelresult.Rating.*;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class ResultRepositoryTest extends RepositoryTest {

    private Message message;
    private Message message1;
    private Message message2;
    private ClothesResult clothes;
    private ClothesResult clothes1;
    private ClothesResult clothes2;

    @BeforeEach
    void setUp() {
        message = new Message(ObjectType.T_SHIRTS, "path", false);
        message1 = new Message(ObjectType.T_SHIRTS, "path", false);
        message2 = new Message(ObjectType.T_SHIRTS, "path", false);
        clothes = new ClothesResult(message);
        clothes1 = new ClothesResult(message1);
        clothes2 = new ClothesResult(message2);

        messageRepository.save(message);
        messageRepository.save(message1);
        messageRepository.save(message2);
    }

    @Test
    @DisplayName("채색 결과의 만족도와 공유 여부를 수정한다.")
    void updateClothes() {
        // given
        Long saveId = resultRepository.save(clothes)
                .getId();
        int expectedRate = FIRST.getValue();
        String expectedReview = "GOOD";
        Boolean expectedShared = true;

        // when
        ClothesResult findClothes = resultRepository.findById(saveId)
                .orElseThrow();
        findClothes.changeRate(expectedRate);
        findClothes.changeShared(expectedShared);
        findClothes.changeReview(expectedReview);

        // then
        Assertions.assertAll(
                () -> assertThat(findClothes.getShared()).isEqualTo(expectedShared),
                () -> assertThat(findClothes.getRating()).isEqualTo(expectedRate),
                () -> assertThat(findClothes.getReview()).isEqualTo(expectedReview)
        );
    }

    @Test
    @DisplayName("전체 검색 개수를 구한다.")
    void count() {
        // given
        resultRepository.save(clothes);
        resultRepository.save(clothes1);
        resultRepository.save(clothes2);

        // then
        assertThat(resultRepository.count()).isEqualTo(3);
    }

    @Test
    @DisplayName("평점 평균을 계산한다.")
    void averageOfRating() {
        // given
        clothes.changeRate(FIRST.getValue());
        clothes1.changeRate(SECOND.getValue());
        clothes2.changeRate(THIRD.getValue());
        resultRepository.save(clothes);
        resultRepository.save(clothes1);
        resultRepository.save(clothes2);

        // then
        assertThat(resultRepository.averageOfRating()).isEqualTo(4L);
    }

    @Test
    @DisplayName("")
    void findAllByShared() {
        // given
        clothes.changeShared(true);
        clothes1.changeShared(true);
        resultRepository.save(clothes);
        resultRepository.save(clothes1);

        // when
        List<ClothesResult> sharedResults = resultRepository.findAllByShared(true);

        // then
        assertThat(sharedResults).extracting("id")
                .containsExactly(clothes.getId(), clothes1.getId());
    }
}