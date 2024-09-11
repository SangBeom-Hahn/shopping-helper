package com.sketch2fashion.backend.config;

import com.sketch2fashion.backend.service.dto.MessageResponseDto;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.embedded.RedisServer;

@TestConfiguration
public class RedisConfig {

    @Value("${spring.redis.data.host}")
    private String host;

    @Value("${spring.redis.data.port}")
    private int port;

    private RedisServer redisServer;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public RedisTemplate<String, MessageResponseDto> redisTemplate() {
        RedisTemplate<String, MessageResponseDto> redisTemplate = new RedisTemplate<>();
        Jackson2JsonRedisSerializer<MessageResponseDto> jsonRedisSerializer =
                new Jackson2JsonRedisSerializer<>(MessageResponseDto.class);

        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(jsonRedisSerializer);
        return redisTemplate;
    }

    @PostConstruct
    public void startRedis() {
        try {
            redisServer = RedisServer.builder()
                    .port(port)
                    .setting("maxmemory 10M")
                    .build();
            redisServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void stopRedis() {
        redisServer.stop();
    }
}
