package com.sketch2fashion.backend.document.dto;

import com.sketch2fashion.backend.controller.dto.ClothesUpdateRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ClothesUpdateRequestTest extends RequestTest{

    @ParameterizedTest
    @NullSource
    @DisplayName("평점으로 null이 들어오면 처리한다.")
    void nullRating(Integer rating) {
        // given
        ClothesUpdateRequest clothesUpdateRequest = new ClothesUpdateRequest(rating, "");

        // then
        assertThat(isEmpty(clothesUpdateRequest)).isTrue();
    }

    @ParameterizedTest
    @EmptySource
    @DisplayName("리뷰값으로 빈 문자열이 들어오면 들어오면 처리한다.")
    void emptyReview(String review) {
        // given
        ClothesUpdateRequest clothesUpdateRequest = new ClothesUpdateRequest(5, review);

        // then
        assertThat(isEmpty(clothesUpdateRequest)).isTrue();
    }
}