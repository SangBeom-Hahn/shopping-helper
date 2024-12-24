package com.sketch2fashion.backend.domain.modelresult;

import com.sketch2fashion.backend.domain.message.Message;
import com.sketch2fashion.backend.domain.message.ObjectType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.sketch2fashion.backend.domain.modelresult.Rating.FIRST;
import static com.sketch2fashion.backend.domain.modelresult.Status.WAIT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ClothesTest {

    @Test
    @DisplayName("채색 결과 메타 데이터의 기본 값을 확인한다.")
    void createAndFineDefaultValue() {
        // given
        Message message = new Message(ObjectType.T_SHIRTS, "storeFilePath", false);

        // when
        ClothesResult clothes = new ClothesResult(message);

        // then
        assertAll(
                () -> assertThat(clothes.getStatus()).isEqualTo(WAIT),
                () -> assertThat(clothes.getStatusMessage()).isEqualTo(WAIT.getMessage()),
                () -> assertThat(clothes.getShared()).isFalse()
        );
    }

    @Test
    @DisplayName("채색 결과를 수정한다.")
    void changeResult() {
        // given
        Message message = new Message(ObjectType.T_SHIRTS, "path", false);
        ClothesResult clothesResult = new ClothesResult(message);
        int expectedRate = FIRST.getValue();
        String expectedReview = "GOOD";
        Boolean expectedShared = true;

        // when
        clothesResult.changeReview(expectedReview);
        clothesResult.changeShared(expectedShared);
        clothesResult.changeRate(expectedRate);

        // then
        Assertions.assertAll(
                () -> assertThat(clothesResult.getReview()).isEqualTo(expectedReview),
                () -> assertThat(clothesResult.getShared()).isEqualTo(expectedShared),
                () -> assertThat(clothesResult.getRating()).isEqualTo(expectedRate)
        );
    }

    @Test
    @DisplayName("채색 결과 메타 데이터를 생성합니다.")
    void construct() {
        // given
        Message message = new Message(ObjectType.T_SHIRTS, "storeFilePath", false);

        // then
        Assertions.assertDoesNotThrow(() -> new ClothesResult(message));
    }
}