package com.sketch2fashion.backend.service.dto;

import com.sketch2fashion.backend.domain.message.Message;
import com.sketch2fashion.backend.domain.message.ObjectType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageResponseDto {

    private Long id;

    private ObjectType objectType;

    private String storeFilePath;

    private Boolean refine;

    public static MessageResponseDto from(Message message) {
        return new MessageResponseDto(
                message.getId(),
                message.getObjectType(),
                message.getStoreFilePath(),
                message.getRefine()
        );
    }
}
