package com.sketch2fashion.backend.domain.upload;

import com.sketch2fashion.backend.domain.message.Message;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.sketch2fashion.backend.domain.message.ObjectType.T_SHIRTS;
import static org.assertj.core.api.Assertions.assertThat;

class ClothesTest {

    @Test
    @DisplayName("채색 요청을 받아서, 업로드 메타 데이터를 생성한다.")
    void construct() {
        // given
        Message message = new Message(T_SHIRTS, "storeFilePath", false);

        // then
        Assertions.assertDoesNotThrow(() -> new Clothes(message, "uploadFileName", "storeFilePath"));
    }

    @Test
    @DisplayName("업로드 데이터의 메세지 ID를 조회한다.")
    void getMessage() {
        // given
        Message message = new Message(T_SHIRTS, "storeFilePath", false);

        // when
        Clothes clothes = new Clothes(message, "uploadFileName", "storeFilePath");

        // then
        assertThat(clothes.getMessageId()).isEqualTo(message.getId());
    }
}