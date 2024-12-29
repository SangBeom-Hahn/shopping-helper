package com.sketch2fashion.backend.service;

import com.sketch2fashion.backend.domain.message.Message;
import com.sketch2fashion.backend.domain.modelresult.ClothesResult;
import com.sketch2fashion.backend.domain.upload.Clothes;
import com.sketch2fashion.backend.exception.NoSuchClothesException;
import com.sketch2fashion.backend.exception.NoSuchMessageException;
import com.sketch2fashion.backend.repository.ClothesRepository;
import com.sketch2fashion.backend.repository.MessageRepository;
import com.sketch2fashion.backend.repository.ResultRepository;
import com.sketch2fashion.backend.service.dto.ClothesResponseDto;
import com.sketch2fashion.backend.service.dto.ClothesSaveResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ClothesService {

    private final ClothesRepository clothesRepository;
    private final MessageRepository messageRepository;
    private final ResultRepository resultRepository;

    public ClothesSaveResponseDto createClothes(final Long messageId, final String storeFilePath, final String uploadFileName) {
        final Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchMessageException(messageId));
        final Clothes clothes = new Clothes(message, uploadFileName, storeFilePath);
        final ClothesResult clothesResult = new ClothesResult(message);

        clothesRepository.save(clothes);
        resultRepository.save(clothesResult);
        return ClothesSaveResponseDto.from(messageId);
    }

    public ClothesResponseDto findClothes(final Long messageId) {
        final Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchMessageException(messageId));
        final Clothes clothes = clothesRepository.findByMessage(message)
                .orElseThrow(() -> new NoSuchClothesException(message.getId()));

        return ClothesResponseDto.from(clothes);
    }
}
