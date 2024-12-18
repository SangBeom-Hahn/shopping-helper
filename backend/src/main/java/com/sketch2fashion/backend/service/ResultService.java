package com.sketch2fashion.backend.service;

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
import com.sketch2fashion.backend.support.consume.dto.InferencesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ResultService {

    private final RedisTemplate<String, ResultResponseDto> redisTemplate;
    private final ClothesRepository  clothesRepository;
    private final MessageRepository messageRepository;
    private final ResultRepository resultRepository;
    private final SearchRepository searchRepository;
    private final SignedUrlBuilder signedUrlBuilder;

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
        ClothesResult clothesResult = resultRepository.findByMessage(message)
                .orElseThrow(() -> new NoSuchMessageException(messageId));

        return ResultResponseDto.of(
                inferencesResponse,
                signedUrlBuilder.generateSignedUrl(clothes.getStoreFilePath()),
                signedUrlBuilder.generateSignedUrl(clothesResult.getStoreFilePath())
        );
    }

    @Transactional(readOnly = true)
    public ResultResponseDto findResult(Long messageId) {
        return redisTemplate.opsForValue()
                .get("RESULT_CACHE::" + messageId);
    }

    public void handlePersistEntity(Long messageId) {
        storeProcess(messageId);
        cleanCacheProcess(messageId);
    }

    private void storeProcess(Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchMessageException(messageId));
        ClothesResult clothesResult = resultRepository.findByMessage(message)
                .orElseThrow(() -> new NoSuchClothesException(messageId));
        validateDuplicateResult(clothesResult);

        findResult(messageId).getInferencesResponse()
                .getResult()
                .forEach(findResponse -> {
                    Search search = new Search(
                            findResponse.getThumbnailUrl(),
                            findResponse.getWebSearchUrl(),
                            findResponse.getHostPageUrl(),
                            findResponse.getSite(),
                            clothesResult
                    );
                    searchRepository.save(search);
                });
    }

    private void cleanCacheProcess(Long messageId) {
        redisTemplate.delete("RESULT_CACHE::" + messageId);
    }

    public void updateResult(Long id, Integer rating, String review) {
        ClothesResult clothesResult = resultRepository.findById(id)
                .orElseThrow(() -> new NoSuchClothesException(id));

        clothesResult.changeReview(review);
        clothesResult.changeRate(rating);
    }

    private void validateDuplicateResult(ClothesResult clothesResult) {
        if(searchRepository.existsByClothes(clothesResult)) {
            throw new DuplicateResultException(clothesResult.getId());
        }
    }
}
