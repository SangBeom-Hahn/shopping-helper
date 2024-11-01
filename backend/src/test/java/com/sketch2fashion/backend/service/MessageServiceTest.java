package com.sketch2fashion.backend.service;

import com.sketch2fashion.backend.domain.message.ObjectType;
import com.sketch2fashion.backend.service.dto.MessageResponseDto;
import com.sketch2fashion.backend.support.MessagePublisher;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

class MessageServiceTest extends ServiceTest {

    @MockBean
    private MessagePublisher fakePublisher;

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
        doNothing().when(fakePublisher).sendModelMessage(any());
        Long saveId = messageService.createMessage(storeFilePath)
                .getId();
        MessageResponseDto messageResponseDto = messageService.findMessage(saveId);

        // then
        assertThat(messageResponseDto).extracting("id", "objectType", "storeFilePath")
                .containsExactly(saveId, ObjectType.SKIRT, storeFilePath);
    }
}