package com.sketch2fashion.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sketch2fashion.backend.service.ResultService;
import com.sketch2fashion.backend.service.dto.MessageResponseDto;
import com.sketch2fashion.backend.support.ClothesModelHttpCaller;
import com.sketch2fashion.backend.support.consume.ClothesJobConsumerListener;
import com.sketch2fashion.backend.support.publish.MessagePublisher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.convert.MappingRedisConverter;
import org.springframework.data.redis.core.convert.RedisConverter;
import org.springframework.data.redis.core.mapping.RedisMappingContext;
import org.springframework.data.redis.hash.ObjectHashMapper;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.sketch2fashion.backend.support.publish.Type.CLOTHES;
import static com.sketch2fashion.backend.utils.SketchConstants.MODEL_RUN_CONSUMER_GROUP;
import static com.sketch2fashion.backend.utils.SketchConstants.MODEL_RUN_CONSUMER_NAME;

@Configuration
public class JobConfig {

    private static final int PROCESSORS = Runtime.getRuntime().availableProcessors();

    @Bean
    public Executor dataRedisStreamExecutor() {
        AtomicInteger index = new AtomicInteger(1);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(PROCESSORS, PROCESSORS, 0, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(), r -> {
            Thread thread = new Thread(r);
            thread.setName("dataRedisConsumer-executor" + index.getAndIncrement());
            thread.setDaemon(true);
            return thread;
        });
        return executor;
    }

    @Bean
    RedisMappingContext redisMappingContext() {
        final RedisMappingContext ctx = new RedisMappingContext();
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
            Executor dataRedisStreamExecutor,
            RedisConnectionFactory redisConnectionFactory,
            ObjectHashMapper hashMapper,
            ClothesModelHttpCaller clothesModelHttpCaller,
            ObjectMapper objectMapper,
            ResultService resultService,
            @Qualifier("streamRedisTemplate") RedisTemplate<String, Object> redisStreamTemplate
    ) {
        try {
            redisStreamTemplate.opsForStream().createGroup(CLOTHES.toString(), MODEL_RUN_CONSUMER_GROUP);
        } catch (RedisSystemException redisSystemException) {
            // ignore
        }

        final StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, ObjectRecord<String, MessageResponseDto>> options =
                StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                .builder()
                .batchSize(1)
                .executor(dataRedisStreamExecutor)
                .keySerializer(new StringRedisSerializer())
                .hashValueSerializer(new StringRedisSerializer())
                .hashValueSerializer(new StringRedisSerializer())
                        .pollTimeout(Duration.ofSeconds(1))
                .objectMapper(hashMapper)
                .targetType(MessageResponseDto.class)
                .build();

        final StreamMessageListenerContainer<String, ObjectRecord<String, MessageResponseDto>> clothesMessageListenerContainer =
                StreamMessageListenerContainer.create(redisConnectionFactory, options);

        final StreamMessageListenerContainer.StreamReadRequest<String> streamReadRequest =
                StreamMessageListenerContainer.StreamReadRequest
                        .builder(StreamOffset.create(CLOTHES.toString(), ReadOffset.lastConsumed()))
                        .consumer(Consumer.from(MODEL_RUN_CONSUMER_GROUP, MODEL_RUN_CONSUMER_NAME))
                        .autoAcknowledge(false)
                        .cancelOnError(throwable -> false)
                        .build();

        clothesMessageListenerContainer.register(streamReadRequest, new ClothesJobConsumerListener(clothesModelHttpCaller, objectMapper, resultService, redisStreamTemplate));

        return clothesMessageListenerContainer;
    }
}
