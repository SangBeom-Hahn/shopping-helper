package com.sketch2fashion.backend.repository;

import com.sketch2fashion.backend.domain.modelresult.ClothesResult;
import com.sketch2fashion.backend.domain.modelresult.Search;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchRepository extends JpaRepository<Search, Long> {

    List<Search> findAllByClothes(final ClothesResult clothes);

    Boolean existsByClothes(final ClothesResult clothes);
}
