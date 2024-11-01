package com.sketch2fashion.backend.repository;

import com.sketch2fashion.backend.domain.message.Message;
import com.sketch2fashion.backend.domain.message.ObjectType;
import com.sketch2fashion.backend.domain.upload.Clothes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ClothesRepositoryTest extends RepositoryTest {

    private Message message;
    private Clothes clothes;

    @BeforeEach
    void setUp() {
        message = new Message(ObjectType.T_SHIRTS, "path");
        clothes = new Clothes(message, "name", "path");

        messageRepository.save(message);
    }

    @Test
    @DisplayName("디자인한 이미지 메타 데이터를 저장한다.")
    void save() {
        // given
        Clothes saveClothes = clothesRepository.save(clothes);
        Message findMessage = saveClothes.getMessage();

        // then
        assertAll(
                () -> assertThat(saveClothes.getId()).isNotNull(),
                () -> assertThat(saveClothes).isEqualTo(clothes),
                () -> assertThat(findMessage.getId()).isEqualTo(message.getId())
        );
    }

    @Test
    @DisplayName("디자인한 이미지 메타 데이터를 저장하고 조회한다.")
    void findById() {
        // given
        Long saveId = clothesRepository.save(clothes)
                .getId();

        // when
        Clothes findClothes = clothesRepository.findById(saveId)
                .orElseThrow();

        // then
        assertThat(findClothes).extracting("id", "status", "statusMessage", "uploadFileName", "storeFilePath")
                .containsExactly(
                        saveId,
                        findClothes.getStatus(),
                        findClothes.getStatusMessage(),
                        findClothes.getUploadFileName(),
                        findClothes.getStoreFilePath()
                );
    }

    @Test
    @DisplayName("추론 요청 메세지 id로 업로드한 메타 데이터를 찾는다.")
    void findByMessage() {
        // given
        Long saveId = clothesRepository.save(clothes)
                .getId();

        // when
        Clothes findClothes = clothesRepository.findByMessage(message)
                .orElseThrow();

        // then
        assertThat(findClothes).extracting("id", "status", "statusMessage", "uploadFileName", "storeFilePath")
                .containsExactly(
                        saveId,
                        findClothes.getStatus(),
                        findClothes.getStatusMessage(),
                        findClothes.getUploadFileName(),
                        findClothes.getStoreFilePath()
                );
    }

    @Test
    @DisplayName("디자인한 이미지 메타 데이터를 삭제한다.")
    void deleteById() {
        // given
        Long saveId = clothesRepository.save(clothes)
                .getId();

        // when
        clothesRepository.deleteById(saveId);

        // then
        assertThat(clothesRepository.findById(saveId))
                .isEmpty();
    }
}