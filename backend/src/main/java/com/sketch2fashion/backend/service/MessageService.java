package com.sketch2fashion.backend.service;

import com.sketch2fashion.backend.domain.message.Message;
import com.sketch2fashion.backend.repository.MessageRepository;
import com.sketch2fashion.backend.service.dto.MessageResponseDto;
import com.sketch2fashion.backend.service.dto.MessageSaveResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sketch2fashion.backend.domain.message.ObjectType.CLOTHES;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    public MessageSaveResponseDto createMessage(String storeFilePath) {
        Message message = new Message(CLOTHES, storeFilePath);
        Long saveId = messageRepository.save(message)
                .getId();

        return MessageSaveResponseDto.from(saveId);
    }

    public MessageResponseDto findMessage(Long id) {
        Message findMessage = messageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException());

        return MessageResponseDto.from(findMessage);
    }
}
