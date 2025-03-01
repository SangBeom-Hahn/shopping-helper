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
import com.sketch2fashion.backend.support.SignedUrlBuilder;
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
    private final SignedUrlBuilder signedUrlBuilder;

    @Transactional(readOnly = true)
    public GallerysResponseDto findAllGallery() {
        final List<GalleryListResponseDto> galleryResponseDtos = resultRepository.findAllByShared(true)
                .stream()
                .map(this::convertGalleryList)
                .collect(Collectors.toList());
        return GallerysResponseDto.from(galleryResponseDtos);
    }

    private GalleryListResponseDto convertGalleryList(final ClothesResult clothesResult) {
        final Message message = clothesResult.getMessage();
        final Clothes clothes = clothesRepository.findByMessage(message)
                .orElseThrow(() -> new NoSuchClothesException(clothesResult.getId()));

        return GalleryListResponseDto.of(
                clothesResult,
                signedUrlBuilder.generateSignedUrl(clothesResult.getStoreFilePath()),
                clothes.getUploadFileName()
        );
    }

    @Transactional(readOnly = true)
    public SearchsResponseDto findAllSearch(final Long id) {
        final ClothesResult clothesResult = resultRepository.findById(id)
                .orElseThrow(() -> new NoSuchClothesException(id));

        final List<SearchResponseDto> searchResponseDtos = searchRepository.findAllByClothes(clothesResult)
                .stream()
                .map(SearchResponseDto::from)
                .collect(Collectors.toList());
        return SearchsResponseDto.from(searchResponseDtos);
    }
}
