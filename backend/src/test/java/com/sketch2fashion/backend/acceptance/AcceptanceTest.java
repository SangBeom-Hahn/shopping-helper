package com.sketch2fashion.backend.acceptance;

import com.sketch2fashion.backend.config.DatabaseCleaner;
import com.sketch2fashion.backend.service.ResultService;
import com.sketch2fashion.backend.support.SignedUrlBuilder;
import com.sketch2fashion.backend.support.UuidHolder;
import com.sketch2fashion.backend.support.publish.MessagePublisher;
import com.sketch2fashion.backend.support.upload.FileUploader;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AcceptanceTest {

    @LocalServerPort
    protected int port;

    @MockBean
    protected FileUploader fakeUploader;

    @MockBean
    protected MessagePublisher fakePublisher;

    @MockBean
    protected SignedUrlBuilder signedUrlBuilder;

    @Autowired
    protected ResultService resultService;

    @Autowired
    protected UuidHolder testUuidHolder;

    @Autowired
    protected DatabaseCleaner databaseCleaner;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        databaseCleaner.execute();
    }
}
