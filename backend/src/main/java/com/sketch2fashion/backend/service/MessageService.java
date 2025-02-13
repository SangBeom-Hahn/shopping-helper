package com.sketch2fashion.backend.service;

import com.sketch2fashion.backend.domain.message.Message;
import com.sketch2fashion.backend.domain.message.ObjectType;
import com.sketch2fashion.backend.exception.NoSuchMessageException;
import com.sketch2fashion.backend.repository.MessageRepository;
import com.sketch2fashion.backend.service.dto.MessageResponseDto;
import com.sketch2fashion.backend.service.dto.MessageSaveResponseDto;
import com.sketch2fashion.backend.support.publish.MessagePublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class MessageService {

    private final MessagePublisher messagePublisher;
    private final MessageRepository messageRepository;

    public MessageSaveResponseDto createMessage(final ObjectType objectType, final String storeFilePath, final Boolean refine) {
        final Message message = new Message(objectType, storeFilePath, refine);
        final Long saveId = messageRepository.save(message)
                .getId();

        this.messagePublisher.sendModelMessage(findMessage(saveId));
        return MessageSaveResponseDto.from(saveId);
    }

    public MessageResponseDto findMessage(final Long id) {
        final Message findMessage = messageRepository.findById(id)
                .orElseThrow(() -> new NoSuchMessageException(id));

        return MessageResponseDto.from(findMessage);
    }
}
