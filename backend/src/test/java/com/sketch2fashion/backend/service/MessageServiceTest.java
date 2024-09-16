package com.sketch2fashion.backend.service;

import com.sketch2fashion.backend.domain.message.ObjectType;
import com.sketch2fashion.backend.service.dto.MessageResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class MessageServiceTest extends ServiceTest {

    @Test
    @DisplayName("존재하지 않는 메세지를 조회하면 예외가 발생한다.")
    void throwException_noSuchMessage() {
        // given
        Long invalidId = 999L;
      
        // then
        assertThatThrownBy(() -> messageService.findMessage(invalidId))
                .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    @DisplayName("메세지를 저장하고 조회한다.")
    void createMessageAndFind() {
        // given
        String storeFilePath = "path";
    
        // when
        Long saveId = messageService.createMessage(storeFilePath)
                .getId();
        MessageResponseDto messageResponseDto = messageService.findMessage(saveId);

        // then
        assertThat(messageResponseDto).extracting("id", "objectType", "storeFilePath")
                .containsExactly(saveId, ObjectType.CLOTHES, "path");
    }
}