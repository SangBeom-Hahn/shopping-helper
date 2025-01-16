package com.sketch2fashion.backend.document.dto;

import com.sketch2fashion.backend.controller.dto.ClothesSharedRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import static com.sketch2fashion.backend.controller.dto.ValidateMessage.EMPTY_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ClothesSharedRequestTest extends RequestTest {

    @ParameterizedTest
    @NullSource
    @DisplayName("공유 여부로 Null이 들어오면 처리한다.")
    void nullShared(Boolean shared) {
        // given
        ClothesSharedRequest clothesSharedRequest = new ClothesSharedRequest(shared);

        // then
        assertThat(isEmpty(clothesSharedRequest)).isTrue();
    }
}