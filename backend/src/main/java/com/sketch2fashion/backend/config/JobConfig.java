package com.sketch2fashion.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sketch2fashion.backend.service.ResultService;
import com.sketch2fashion.backend.service.dto.MessageResponseDto;
import com.sketch2fashion.backend.support.ClothesModelHttpCaller;
import com.sketch2fashion.backend.support.consume.ClothesJobConsumerListener;
import com.sketch2fashion.backend.support.publish.Type;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.convert.MappingRedisConverter;
import org.springframework.data.redis.core.convert.RedisConverter;
import org.springframework.data.redis.core.mapping.RedisMappingContext;
import org.springframework.data.redis.hash.ObjectHashMapper;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;

import java.util.Collections;

import static com.sketch2fashion.backend.support.publish.Type.CLOTHES;

@Configuration
public class JobConfig {

    @Bean
    RedisMappingContext redisMappingContext() {
        RedisMappingContext ctx = new RedisMappingContext();
        ctx.setInitialEntitySet(Collections.singleton(MessageResponseDto.class));
        return ctx;
    }

    @Bean
    RedisConverter redisConverter(RedisMappingContext redisMappingContext) {
        return new MappingRedisConverter(redisMappingContext);
    }

    @Bean
    ObjectHashMapper hashMapper(RedisConverter converter) {
        return new ObjectHashMapper(converter);
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public StreamMessageListenerContainer<String, ObjectRecord<String, MessageResponseDto>> streamMessageListenerContainer(
            RedisConnectionFactory redisConnectionFactory,
            ObjectHashMapper hashMapper,
            ClothesModelHttpCaller clothesModelHttpCaller,
            ObjectMapper objectMapper,
            ResultService resultService
    ) {
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, ObjectRecord<String, MessageResponseDto>> options =
                StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                .builder()
                .batchSize(1)
                .keySerializer(new StringRedisSerializer())
                .hashValueSerializer(new StringRedisSerializer())
                .hashValueSerializer(new StringRedisSerializer())
                .objectMapper(hashMapper)
                .targetType(MessageResponseDto.class)
                .build();

        StreamMessageListenerContainer<String, ObjectRecord<String, MessageResponseDto>> clothesMessageListenerContainer =
                StreamMessageListenerContainer.create(redisConnectionFactory, options);

        StreamMessageListenerContainer.StreamReadRequest<String> streamReadRequest =
                StreamMessageListenerContainer.StreamReadRequest
                        .builder(StreamOffset.create(CLOTHES.toString(), ReadOffset.lastConsumed()))
                        .build();

        clothesMessageListenerContainer.register(streamReadRequest, new ClothesJobConsumerListener(clothesModelHttpCaller, objectMapper, resultService));
        return clothesMessageListenerContainer;
    }
}
