package com.sketch2fashion.backend.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelConfig {

    @Value("${worker.uri.tshirts-server}")
    private String tshirtsWorkerUri;

    @Value("${worker.uri.pants-server}")
    private String pantsWorkerUri;

    @Value("${worker.uri.hat-server}")
    private String hatWorkerUri;

    @Value("${worker.uri.skirt-server}")
    private String skirtWorkerUri;

    public static String TSHIRTS_WORKER_URI;
    public static String PANTS_WORKER_URI;
    public static String HAT_WORKER_URI;
    public static String SKIRT_WORKER_URI;

    @PostConstruct
    public void init() {
        TSHIRTS_WORKER_URI = tshirtsWorkerUri;
        PANTS_WORKER_URI = pantsWorkerUri;
        HAT_WORKER_URI = hatWorkerUri;
        SKIRT_WORKER_URI = skirtWorkerUri;
    }
}
