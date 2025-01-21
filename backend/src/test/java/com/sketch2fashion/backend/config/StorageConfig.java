package com.sketch2fashion.backend.config;

import com.google.cloud.storage.Storage;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class StorageConfig {

    @Bean
    public static Storage mockStorage() {
        return mock(Storage.class);
    }
}
