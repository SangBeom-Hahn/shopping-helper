package com.sketch2fashion.backend.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ClothesSharedRequest {

    @NotNull
    private Boolean shared;
}
