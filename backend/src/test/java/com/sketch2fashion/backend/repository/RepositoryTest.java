package com.sketch2fashion.backend.repository;

import com.sketch2fashion.backend.config.JpaAuditingConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(JpaAuditingConfig.class)
public abstract class RepositoryTest {

    @Autowired
    protected MessageRepository messageRepository;
}
