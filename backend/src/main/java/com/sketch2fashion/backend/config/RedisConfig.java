package com.sketch2fashion.backend.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.sketch2fashion.backend.service.dto.MessageResponseDto;
import com.sketch2fashion.backend.service.dto.MessageSaveResponseDto;
import com.sketch2fashion.backend.service.dto.ResultResponseDto;
import com.sketch2fashion.backend.support.consume.dto.InferencesResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.lang.runtime.ObjectMethods;

@Configuration
@EnableCaching
public class RedisConfig {

    @Value("${spring.redis.data.host}")
    private String host;

    @Value("${spring.redis.data.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(host, port));
    }

    @Bean
    public Jackson2JsonRedisSerializer<ResultResponseDto> jsonRedisSerializer() {
        return new Jackson2JsonRedisSerializer<>(ResultResponseDto.class);
    }

    @Bean
    public RedisCacheManager cacheManager(
            RedisConnectionFactory redisConnectionFactory,
            Jackson2JsonRedisSerializer<ResultResponseDto> jsonRedisSerializer
    ) {
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jsonRedisSerializer));

        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory)
                .cacheDefaults(configuration)
                .build();
    }

    @Bean
    public RedisTemplate<String, ResultResponseDto> redisTemplate(
            RedisConnectionFactory redisConnectionFactory,
            Jackson2JsonRedisSerializer<ResultResponseDto> jsonRedisSerializer
    ) {
        RedisTemplate<String, ResultResponseDto> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(jsonRedisSerializer);
        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, Object> streamRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
