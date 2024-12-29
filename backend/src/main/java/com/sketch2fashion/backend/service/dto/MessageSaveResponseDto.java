package com.sketch2fashion.backend.service.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageSaveResponseDto {

    private Long id;

    public static MessageSaveResponseDto from(final Long id) {
        return new MessageSaveResponseDto(id);
    }
}
