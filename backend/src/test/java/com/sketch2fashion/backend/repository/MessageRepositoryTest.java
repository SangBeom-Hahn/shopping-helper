package com.sketch2fashion.backend.repository;

import com.sketch2fashion.backend.domain.message.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.sketch2fashion.backend.domain.message.ObjectType.T_SHIRTS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class MessageRepositoryTest extends RepositoryTest {

    private Message message;

    @BeforeEach
    void setUp() {
        message = new Message(T_SHIRTS, "path", false);
    }

    @Test
    @DisplayName("메세지를 저장한다.")
    void save() {
        // given
        Message saveMessage = messageRepository.save(message);

        // then
        assertAll(
                () -> assertThat(saveMessage.getId()).isNotNull(),
                () -> assertThat(saveMessage).isEqualTo(message)
        );
    }

    @Test
    @DisplayName("메세지를 조회한다.")
    void findById() {
        // given
        Long saveId = messageRepository.save(message)
                .getId();

        // when
        Message findMessage = messageRepository.findById(message.getId())
                .orElseThrow();

        // then
        assertThat(findMessage).extracting("id", "objectType", "storeFilePath")
                .containsExactly(saveId, T_SHIRTS, "path");
    }
    
    @Test
    @DisplayName("메세지를 삭제한다.")
    void delete() {
        // given
        Long saveId = messageRepository.save(message)
                .getId();

        // when
        messageRepository.deleteById(saveId);
      
        // then
        assertThat(messageRepository.findById(saveId))
                .isEmpty();
    }
}