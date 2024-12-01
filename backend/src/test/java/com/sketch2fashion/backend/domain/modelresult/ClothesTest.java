package com.sketch2fashion.backend.domain.modelresult;

import com.sketch2fashion.backend.domain.message.Message;
import com.sketch2fashion.backend.domain.message.ObjectType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.sketch2fashion.backend.domain.modelresult.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ClothesTest {

    @Test
    @DisplayName("채색 결과 메타 데이터의 기본 값을 확인한다.")
    void createAndFineDefaultValue() {
        // given
        Message message = new Message(ObjectType.T_SHIRTS, "storeFilePath", false);

        // when
        Clothes clothes = new Clothes(message, "storeFilePath");

        // then
        assertAll(
                () -> assertThat(clothes.getStatus()).isEqualTo(WAIT),
                () -> assertThat(clothes.getStatusMessage()).isEqualTo(WAIT.getMessage()),
                () -> assertThat(clothes.getRating()).isEqualTo(Rating.THIRD),
                () -> assertThat(clothes.getShared()).isFalse()
        );
    }

    @Test
    @DisplayName("채색 결과 메타 데이터를 생성합니다.")
    void construct() {
        // given
        Message message = new Message(ObjectType.T_SHIRTS, "storeFilePath", false);

        // then
        Assertions.assertDoesNotThrow(() -> new Clothes(message, "storeFilePath"));
    }
}