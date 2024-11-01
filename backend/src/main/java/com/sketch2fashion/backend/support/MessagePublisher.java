package com.sketch2fashion.backend.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sketch2fashion.backend.domain.message.ObjectType;
import com.sketch2fashion.backend.service.dto.MessageResponseDto;
import com.sketch2fashion.backend.service.dto.MessageSaveResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static com.sketch2fashion.backend.domain.message.ObjectType.SKIRT;

@Component
@RequiredArgsConstructor
public class MessagePublisher {

    private final RedisTemplate<String, MessageResponseDto> redisTemplate;

    @Async
    public void sendModelMessage(MessageResponseDto message) {
        redisTemplate.opsForList().leftPush(
                SKIRT.getValue(),
                message
        );
    }
}
