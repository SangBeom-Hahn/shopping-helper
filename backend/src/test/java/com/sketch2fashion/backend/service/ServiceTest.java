package com.sketch2fashion.backend.service;

import com.sketch2fashion.backend.config.DatabaseCleaner;
import com.sketch2fashion.backend.repository.ClothesRepository;
import com.sketch2fashion.backend.repository.MessageRepository;
import com.sketch2fashion.backend.repository.ResultRepository;
import com.sketch2fashion.backend.repository.SearchRepository;
import com.sketch2fashion.backend.service.dto.ResultResponseDto;
import com.sketch2fashion.backend.support.upload.FileUploader;
import com.sketch2fashion.backend.support.SignedUrlBuilder;
import com.sketch2fashion.backend.support.publish.MessagePublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
public abstract class ServiceTest {

    @MockBean
    protected FileUploader fakeUploader;

    @MockBean
    protected MessagePublisher fakePublisher;

    @MockBean
    protected SignedUrlBuilder signedUrlBuilder;

    @Autowired
    protected ClothesService clothesService;

    @Autowired
    protected MessageService messageService;

    @Autowired
    protected MessageRepository messageRepository;

    @Autowired
    protected ResultService resultService;

    @Autowired
    protected GalleryService galleryService;

    @Autowired
    protected ResultRepository resultRepository;

    @Autowired
    protected SearchRepository searchRepository;

    @Autowired
    protected ClothesRepository clothesRepository;

    @Autowired
    protected RedisTemplate<String, ResultResponseDto> redisTemplate;

    @Autowired
    protected DatabaseCleaner databaseCleaner;
}
