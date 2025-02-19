package com.sketch2fashion.backend.config;

import com.sketch2fashion.backend.controller.history.HistoryInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@TestConfiguration
public class WebConfig implements WebMvcConfigurer {
}
