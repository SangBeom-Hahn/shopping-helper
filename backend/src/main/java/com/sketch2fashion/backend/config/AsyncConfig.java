package com.sketch2fashion.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

import static com.sketch2fashion.backend.utils.SketchConstants.QUEUE_PUBLISH_THREAD_NAME;

@EnableAsync
@Configuration
public class AsyncConfig {

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix(QUEUE_PUBLISH_THREAD_NAME);
        executor.initialize();
        return executor;
    }
}
