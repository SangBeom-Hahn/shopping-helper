package com.sketch2fashion.backend.domain.modelresult;

import com.sketch2fashion.backend.domain.message.Message;
import com.sketch2fashion.backend.domain.message.ObjectType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SearchTest {

    @Test
    @DisplayName("검색 결과를 생성한다.")
    void construct() {
        // given
        Message message = new Message(ObjectType.T_SHIRTS, "path", false);
        ClothesResult clothes = new ClothesResult(message);

        // then
        assertDoesNotThrow(() -> new Search(
                "url",
                "url",
                "url",
                "name",
                "site",
                clothes
        ));
    }
}