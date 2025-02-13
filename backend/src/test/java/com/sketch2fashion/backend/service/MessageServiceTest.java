package com.sketch2fashion.backend.service;

import com.sketch2fashion.backend.domain.message.ObjectType;
import com.sketch2fashion.backend.exception.NoSuchMessageException;
import com.sketch2fashion.backend.service.dto.MessageResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.sketch2fashion.backend.domain.message.ObjectType.SKIRT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

class MessageServiceTest extends ServiceTest {

    @Test
    @DisplayName("존재하지 않는 메세지를 조회하면 예외가 발생한다.")
    void throwException_noSuchMessage() {
        // given
        Long invalidId = 999L;
      
        // then
        assertThatThrownBy(() -> messageService.findMessage(invalidId))
                .isInstanceOf(NoSuchMessageException.class);
    }
    
    @Test
    @DisplayName("메세지를 저장하고 조회한다.")
    void createMessageAndFind() {
        // given
        String storeFilePath = "path";
        ObjectType objectType = SKIRT;
        Boolean refine = false;
    
        // when
        doNothing().when(fakePublisher).sendModelMessage(any());
        Long saveId = messageService.createMessage(objectType, storeFilePath, refine)
                .getId();
        MessageResponseDto messageResponseDto = messageService.findMessage(saveId);

        // then
        assertThat(messageResponseDto).extracting("id", "objectType", "storeFilePath", "refine")
                .containsExactly(saveId, objectType, storeFilePath, refine);
    }
}