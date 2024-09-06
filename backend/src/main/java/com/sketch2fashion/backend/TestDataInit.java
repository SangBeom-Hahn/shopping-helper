package com.sketch2fashion.backend;

import com.sketch2fashion.backend.domain.message.Message;
import com.sketch2fashion.backend.domain.message.ObjectType;
import com.sketch2fashion.backend.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@RequiredArgsConstructor
public class TestDataInit {

    private final MessageRepository messageRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        Message message = new Message(ObjectType.CLOTHES, "storeFilePath");
        messageRepository.save(message);
    }
}
