package com.sketch2fashion.backend.repository;

import com.sketch2fashion.backend.domain.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
