package com.sketch2fashion.backend.service;

import com.sketch2fashion.backend.repository.MessageRepository;
import com.sketch2fashion.backend.support.FileUploader;
import com.sketch2fashion.backend.support.publish.MessagePublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
public abstract class ServiceTest {

    @MockBean
    protected FileUploader fakeUploader;

    @MockBean
    protected MessagePublisher fakePublisher;

    @Autowired
    protected ClothesService clothesService;

    @Autowired
    protected MessageService messageService;

    @Autowired
    protected MessageRepository messageRepository;
}
