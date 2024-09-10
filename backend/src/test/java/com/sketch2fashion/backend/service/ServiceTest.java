package com.sketch2fashion.backend.service;

import com.sketch2fashion.backend.repository.ClothesRepository;
import com.sketch2fashion.backend.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
public abstract class ServiceTest {

    @Autowired
    protected ClothesService clothesService;

    @Autowired
    protected MessageService messageService;

    @Autowired
    protected MessageRepository messageRepository;
}
