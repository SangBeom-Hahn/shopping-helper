package com.sketch2fashion.backend.service;

import com.sketch2fashion.backend.domain.message.Message;
import com.sketch2fashion.backend.domain.upload.Clothes;
import com.sketch2fashion.backend.exception.NoSuchClothesException;
import com.sketch2fashion.backend.exception.NoSuchMessageException;
import com.sketch2fashion.backend.repository.ClothesRepository;
import com.sketch2fashion.backend.repository.MessageRepository;
import com.sketch2fashion.backend.repository.ResultRepository;
import com.sketch2fashion.backend.service.dto.ResultResponseDto;
import com.sketch2fashion.backend.support.consume.dto.InferencesResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ResultService {

    private final RedisTemplate<String, InferencesResponse> redisTemplate;
    private final ClothesRepository  clothesRepository;
    private final MessageRepository messageRepository;
    private final ResultRepository resultRepository;

    @Cacheable(
            value = "RESULT_CACHE",
            key = "#messageId",
            cacheManager = "cacheManager"
    )
    public ResultResponseDto saveResult(Long messageId, InferencesResponse inferencesResponse) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchMessageException(messageId));
        Clothes clothes = clothesRepository.findByMessage(message)
                .orElseThrow(() -> new NoSuchClothesException(messageId));
        String resultImageStoreFilePath = resultRepository.findByMessage(message)
                .orElseThrow(() -> new NoSuchMessageException(messageId))
                .getStoreFilePath();

        return ResultResponseDto.of(
                inferencesResponse,
                clothes.getStoreFilePath(),
                resultImageStoreFilePath
        );
    }
}
