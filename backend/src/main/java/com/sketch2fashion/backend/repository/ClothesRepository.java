package com.sketch2fashion.backend.repository;

import com.sketch2fashion.backend.domain.message.Message;
import com.sketch2fashion.backend.domain.upload.Clothes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClothesRepository extends JpaRepository<Clothes, Long> {

    Optional<Clothes> findByMessage(final Message message);
}
