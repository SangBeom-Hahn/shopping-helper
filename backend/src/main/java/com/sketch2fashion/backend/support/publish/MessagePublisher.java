package com.sketch2fashion.backend.support.publish;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sketch2fashion.backend.domain.message.ObjectType;
import com.sketch2fashion.backend.service.dto.MessageResponseDto;
import com.sketch2fashion.backend.service.dto.MessageSaveResponseDto;
import com.sketch2fashion.backend.support.publish.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.hash.Jackson2HashMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static com.sketch2fashion.backend.domain.message.ObjectType.SKIRT;
import static com.sketch2fashion.backend.support.publish.Type.CLOTHES;

@Component
@RequiredArgsConstructor
public class MessagePublisher {

    private final RedisTemplate<String, MessageResponseDto> redisTemplate;

    @Async
    public void sendModelMessage(MessageResponseDto message) {
        ObjectRecord<String, MessageResponseDto> record = StreamRecords.newRecord()
                .in(CLOTHES.toString())
                .ofObject(message)
            .withId(RecordId.autoGenerate());

        redisTemplate.opsForStream(new Jackson2HashMapper(true))
                .add(record);
    }
}
