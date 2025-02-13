package com.sketch2fashion.backend.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.sketch2fashion.backend.controller.dto.ValidateMessage.EMPTY_MESSAGE;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ClothesUpdateRequest {

    @NotNull(message = EMPTY_MESSAGE)
    private Integer rating;

    @NotBlank(message = EMPTY_MESSAGE)
    private String review;
}
