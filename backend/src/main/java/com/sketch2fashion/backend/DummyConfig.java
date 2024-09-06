package com.sketch2fashion.backend;

import com.sketch2fashion.backend.repository.MessageRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DummyConfig {

    @Bean
    public TestDataInit testDataInit(MessageRepository messageRepository) {
        return new TestDataInit(messageRepository);
    }
}
