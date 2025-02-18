package com.sketch2fashion.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sketch2fashion.backend.domain.message.Message;
import com.sketch2fashion.backend.domain.modelresult.ClothesResult;
import com.sketch2fashion.backend.domain.modelresult.Search;
import com.sketch2fashion.backend.domain.upload.Clothes;
import com.sketch2fashion.backend.exception.DuplicateResultException;
import com.sketch2fashion.backend.exception.NoSuchClothesException;
import com.sketch2fashion.backend.exception.NoSuchMessageException;
import com.sketch2fashion.backend.repository.ClothesRepository;
import com.sketch2fashion.backend.repository.MessageRepository;
import com.sketch2fashion.backend.repository.ResultRepository;
import com.sketch2fashion.backend.repository.SearchRepository;
import com.sketch2fashion.backend.service.dto.ResultResponseDto;
import com.sketch2fashion.backend.support.SignedUrlBuilder;
import com.sketch2fashion.backend.support.consume.dto.ErrorResponse;
import com.sketch2fashion.backend.support.consume.dto.InferencesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sketch2fashion.backend.utils.SketchConstants.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ResultService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ClothesRepository  clothesRepository;
    private final MessageRepository messageRepository;
    private final ResultRepository resultRepository;
    private final SearchRepository searchRepository;
    private final SignedUrlBuilder signedUrlBuilder;
    private final ObjectMapper objectMapper;

    @Cacheable(
            value = SEARCH_RESULT_CACHE,
            key = "#messageId",
            cacheManager = CACHE_MANAGER_NAME
    )
    public ResultResponseDto saveResult(final Long messageId, final InferencesResponse inferencesResponse) {
        final Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchMessageException(messageId));
        final Clothes clothes = clothesRepository.findByMessage(message)
                .orElseThrow(() -> new NoSuchClothesException(messageId));
        final ClothesResult clothesResult = resultRepository.findByMessage(message)
                .orElseThrow(() -> new NoSuchMessageException(messageId));

        return ResultResponseDto.of(
                inferencesResponse,
                signedUrlBuilder.generateSignedUrl(clothes.getStoreFilePath()),
                signedUrlBuilder.generateSignedUrl(clothesResult.getStoreFilePath())
        );
    }

    @Cacheable(
            value = SEARCH_RESULT_CACHE,
            key = "#messageId",
            cacheManager = CACHE_MANAGER_NAME
    )
    public ErrorResponse saveErrorResult(final Long messageId) {
        final Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchMessageException(messageId));
        final ClothesResult clothesResult = resultRepository.findByMessage(message)
                .orElseThrow(() -> new NoSuchMessageException(messageId));
        return ErrorResponse.from(clothesResult.getStatusMessage());
    }

    @Transactional(readOnly = true)
    public Object findResult(final Long messageId) {
        return redisTemplate.opsForValue()
                .get(KEY_PREFIX + messageId);
    }

    public void handlePersistEntity(final Long messageId) {
        storeProcess(messageId);
        cleanCacheProcess(messageId);
    }

    private void storeProcess(final Long messageId) {
        final Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchMessageException(messageId));
        final ClothesResult clothesResult = resultRepository.findByMessage(message)
                .orElseThrow(() -> new NoSuchClothesException(messageId));
        validateDuplicateResult(clothesResult);

        objectMapper.convertValue(findResult(messageId), ResultResponseDto.class)
                .getInferencesResponse()
                .getResult()
                .forEach(findResponse -> {
                    final Search search = new Search(
                            findResponse.getThumbnailUrl(),
                            findResponse.getWebSearchUrl(),
                            findResponse.getHostPageUrl(),
                            findResponse.getName(),
                            findResponse.getSite(),
                            clothesResult
                    );
                    searchRepository.save(search);
                });
    }

    private void cleanCacheProcess(final Long messageId) {
        redisTemplate.delete(KEY_PREFIX + messageId);
    }

    public void updateResult(final Long id, final Integer rating, final String review) {
        final ClothesResult clothesResult = resultRepository.findById(id)
                .orElseThrow(() -> new NoSuchClothesException(id));

        clothesResult.changeReview(review);
        clothesResult.changeRate(rating);
    }

    public void updateShared(final Long id, final Boolean shared) {
        final ClothesResult clothesResult = resultRepository.findById(id)
                .orElseThrow(() -> new NoSuchClothesException(id));

        clothesResult.changeShared(shared);
    }

    private void validateDuplicateResult(final ClothesResult clothesResult) {
        if(searchRepository.existsByClothes(clothesResult)) {
            throw new DuplicateResultException(clothesResult.getId());
        }
    }
}
