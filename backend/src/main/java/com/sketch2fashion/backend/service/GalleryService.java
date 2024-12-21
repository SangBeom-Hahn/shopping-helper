package com.sketch2fashion.backend.service;

import com.sketch2fashion.backend.domain.message.Message;
import com.sketch2fashion.backend.domain.modelresult.ClothesResult;
import com.sketch2fashion.backend.domain.upload.Clothes;
import com.sketch2fashion.backend.exception.NoSuchClothesException;
import com.sketch2fashion.backend.repository.ClothesRepository;
import com.sketch2fashion.backend.repository.ResultRepository;
import com.sketch2fashion.backend.repository.SearchRepository;
import com.sketch2fashion.backend.service.dto.GalleryListResponseDto;
import com.sketch2fashion.backend.service.dto.GallerysResponseDto;
import com.sketch2fashion.backend.service.dto.SearchResponseDto;
import com.sketch2fashion.backend.service.dto.SearchsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class GalleryService {

    private final ResultRepository resultRepository;
    private final ClothesRepository clothesRepository;
    private final SearchRepository searchRepository;

    public GallerysResponseDto findAllGallery() {
        List<GalleryListResponseDto> galleryResponseDtos = resultRepository.findAllByShared(true)
                .stream()
                .map(this::convertGalleryList)
                .collect(Collectors.toList());
        return GallerysResponseDto.from(galleryResponseDtos);
    }

    private GalleryListResponseDto convertGalleryList(ClothesResult clothesResult) {
        Message message = clothesResult.getMessage();
        Clothes clothes = clothesRepository.findByMessage(message)
                .orElseThrow(() -> new NoSuchClothesException(clothesResult.getId()));

        return GalleryListResponseDto.of(clothesResult, clothes.getUploadFileName());
    }

    public SearchsResponseDto findAllSearch(Long id) {
        ClothesResult clothesResult = resultRepository.findById(id)
                .orElseThrow(() -> new NoSuchClothesException(id));

        List<SearchResponseDto> searchResponseDtos = searchRepository.findAllByClothes(clothesResult)
                .stream()
                .map(SearchResponseDto::from)
                .collect(Collectors.toList());
        return SearchsResponseDto.from(searchResponseDtos);
    }
}
