package com.sketch2fashion.backend.repository;

import com.sketch2fashion.backend.domain.upload.Clothes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClothesRepository extends JpaRepository<Clothes, Long> {
}
