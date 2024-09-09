package com.sketch2fashion.backend.repository;

import com.sketch2fashion.backend.domain.message.Message;
import com.sketch2fashion.backend.domain.message.ObjectType;
import com.sketch2fashion.backend.domain.upload.Clothes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ClothesRepositoryTest extends RepositoryTest {

    private Message message;
    private Clothes clothes;

    @BeforeEach
    void setUp() {
        message = new Message(ObjectType.CLOTHES, "path");
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
        assertThat(findClothes).extracting("id", "uploadFileName", "storeFilePath")
                .containsExactly(saveId, "name", "path");
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