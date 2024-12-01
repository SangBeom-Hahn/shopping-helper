package com.sketch2fashion.backend.repository;

import com.sketch2fashion.backend.domain.message.Message;
import com.sketch2fashion.backend.domain.message.ObjectType;
import com.sketch2fashion.backend.domain.modelresult.ClothesResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.sketch2fashion.backend.domain.modelresult.Rating.FIRST;
import static org.assertj.core.api.Assertions.assertThat;

class ResultRepositoryTest extends RepositoryTest {

    private Message message;
    private ClothesResult clothes;

    @BeforeEach
    void setUp() {
        message = new Message(ObjectType.T_SHIRTS, "path", false);
        clothes = new ClothesResult(message);

        messageRepository.save(message);
    }

    @Test
    @DisplayName("채색 결과의 만족도와 공유 여부를 수정한다.")
    void updateClothes() {
        // given
        Long saveId = resultRepository.save(clothes)
                .getId();

        // when
        ClothesResult findClothes = resultRepository.findById(saveId)
                .orElseThrow();
        findClothes.changeRate(FIRST);
        findClothes.changeShared(true);

        // then
        Assertions.assertAll(
                () -> assertThat(findClothes.getShared()).isEqualTo(true),
                () -> assertThat(findClothes.getRating()).isEqualTo(FIRST)
        );
    }
}