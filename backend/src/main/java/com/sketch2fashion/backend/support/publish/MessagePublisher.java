package com.sketch2fashion.backend.support.publish;

import com.sketch2fashion.backend.service.dto.MessageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static com.sketch2fashion.backend.support.publish.Type.CLOTHES;

@Component
@RequiredArgsConstructor
public class MessagePublisher {

    private final RedisTemplate<String, Object> streamRedisTemplate;

    @Async
    public void sendModelMessage(final MessageResponseDto message) {
        final ObjectRecord<String, MessageResponseDto> record = StreamRecords.newRecord()
                .in(CLOTHES.toString())
                .ofObject(message)
                .withId(RecordId.autoGenerate());

        streamRedisTemplate.opsForStream()
                .add(record);
    }
}
