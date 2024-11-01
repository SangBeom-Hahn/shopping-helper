package com.sketch2fashion.backend.domain.message;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest {

    @Test
    @DisplayName("추론 요청을 받아서, 메세지를 생성한다.")
    void construct() {
        Assertions.assertDoesNotThrow(() -> new Message(ObjectType.SKIRT, "storeFilePath"));
    }
}