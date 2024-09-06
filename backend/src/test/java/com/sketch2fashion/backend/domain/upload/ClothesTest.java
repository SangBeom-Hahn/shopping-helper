package com.sketch2fashion.backend.domain.upload;

import com.sketch2fashion.backend.domain.message.Message;
import com.sketch2fashion.backend.domain.message.ObjectType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClothesTest {

    @Test
    @DisplayName("채색 요청을 받아서, 업로드 메타 데이터를 생성한다.")
    void construct() {
        // given
        Message message = new Message(ObjectType.CLOTHES, "storeFilePath");

        // then
        Assertions.assertDoesNotThrow(() -> new Clothes(message, "uploadFileName", "storeFilePath"));
    }
}