package com.sketch2fashion.backend.domain.message;

import com.sketch2fashion.backend.exception.NoSuchTypeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ObjectTypeTest {

    @Test
    @DisplayName("[T_SHIRTS, PANTS, SKIRT, HAT]이 아닌 타입으로 업로드 할 경우 예외가 발생한다.")
    void throwException_NoSuchType() {
        // given
        String invalidType = "BAG";

        // then
        assertThatThrownBy(() -> ObjectType.from(invalidType))
                .isInstanceOf(NoSuchTypeException.class)
                .hasMessage(String.format("존재하지 않는 TYPE 입니다. type = {%s}", invalidType));
    }

    @ParameterizedTest
    @DisplayName("올바른 TYPE을 생성한다.")
    @ValueSource(strings = {"T_SHIRTS", "PANTS", "SKIRT", "HAT"})
    void construct(String validType) {
        assertDoesNotThrow(() -> ObjectType.from(validType));
    }
}