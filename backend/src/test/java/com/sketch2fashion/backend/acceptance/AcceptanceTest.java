package com.sketch2fashion.backend.acceptance;

import com.sketch2fashion.backend.repository.MessageRepository;
import com.sketch2fashion.backend.repository.ResultRepository;
import com.sketch2fashion.backend.service.MessageService;
import com.sketch2fashion.backend.service.ResultService;
import com.sketch2fashion.backend.service.dto.ResultResponseDto;
import com.sketch2fashion.backend.support.FileUploader;
import com.sketch2fashion.backend.support.publish.MessagePublisher;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.client.RestTemplate;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AcceptanceTest {

    @LocalServerPort
    protected int port;

    @MockBean
    protected FileUploader fakeUploader;

    @MockBean
    protected MessagePublisher fakePublisher;

    @Autowired
    protected RedisTemplate<String, ResultResponseDto> redisTemplate;

    @Autowired
    protected MessageRepository messageRepository;

    @Autowired
    protected ResultRepository resultRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }
}
