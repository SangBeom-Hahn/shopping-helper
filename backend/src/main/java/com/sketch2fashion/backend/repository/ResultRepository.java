package com.sketch2fashion.backend.repository;

import com.sketch2fashion.backend.domain.message.Message;
import com.sketch2fashion.backend.domain.modelresult.ClothesResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResultRepository extends JpaRepository<ClothesResult, Long> {

    Optional<ClothesResult> findByMessage(Message message);
}
