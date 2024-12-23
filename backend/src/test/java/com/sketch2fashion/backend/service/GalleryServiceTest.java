package com.sketch2fashion.backend.service;

import com.sketch2fashion.backend.domain.message.Message;
import com.sketch2fashion.backend.domain.message.ObjectType;
import com.sketch2fashion.backend.domain.modelresult.ClothesResult;
import com.sketch2fashion.backend.domain.modelresult.Search;
import com.sketch2fashion.backend.domain.upload.Clothes;
import com.sketch2fashion.backend.service.dto.GalleryListResponseDto;
import com.sketch2fashion.backend.service.dto.GallerysResponseDto;
import com.sketch2fashion.backend.service.dto.SearchResponseDto;
import com.sketch2fashion.backend.service.dto.SearchsResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.sketch2fashion.backend.domain.message.ObjectType.T_SHIRTS;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class GalleryServiceTest extends ServiceTest {

    private Message message;
    private Message message1;
    private Message message2;
    private Message message3;
    private ClothesResult clothesResult;
    private ClothesResult clothesResult1;
    private ClothesResult clothesResult2;
    private ClothesResult clothesResult3;
    private Clothes clothes;
    private Clothes clothes1;
    private Clothes clothes2;
    private Search search;
    private Search search1;

    @BeforeEach
    void setUp() {
        message = new Message(T_SHIRTS, "path", false);
        message1 = new Message(T_SHIRTS, "path", false);
        message2 = new Message(T_SHIRTS, "path", false);
        message3 = new Message(T_SHIRTS, "path", false);
        clothes = new Clothes(message, "name1", "path");
        clothes1 = new Clothes(message1, "name2", "path");
        clothes2 = new Clothes(message2, "name3", "path");
        clothesResult = new ClothesResult(message);
        clothesResult1 = new ClothesResult(message1);
        clothesResult2 = new ClothesResult(message2);
        clothesResult3 = new ClothesResult(message3);
        search = new Search("url", "url", "url", "name", "site", clothesResult3);
        search1 = new Search("url", "url", "url", "name", "site", clothesResult3);

        messageRepository.save(message);
        messageRepository.save(message1);
        messageRepository.save(message2);
        messageRepository.save(message3);
        clothesRepository.save(clothes);
        clothesRepository.save(clothes1);
        clothesRepository.save(clothes2);
        resultRepository.save(clothesResult3);
        searchRepository.save(search);
        searchRepository.save(search1);
    }

    @Test
    @DisplayName("공유 여부가 [true]인 채색 결과를 갤러리에 보여준다.")
    void findAllGallery() {
        // given
        clothesResult.changeShared(true);
        clothesResult1.changeShared(true);
        clothesResult2.changeShared(true);
        resultRepository.save(clothesResult);
        resultRepository.save(clothesResult1);
        resultRepository.save(clothesResult2);

        List<GalleryListResponseDto> galleryListResponseDtos = List.of(
                GalleryListResponseDto.of(clothesResult, clothesResult.getStoreFilePath(), clothes.getUploadFileName()),
                GalleryListResponseDto.of(clothesResult1, clothesResult1.getStoreFilePath(), clothes1.getUploadFileName()),
                GalleryListResponseDto.of(clothesResult2, clothesResult2.getStoreFilePath(), clothes2.getUploadFileName())
        );
        GallerysResponseDto expected = GallerysResponseDto.from(galleryListResponseDtos);

        // when
        GallerysResponseDto actual = galleryService.findAllGallery();

        // then
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("공유한 채색 결과물을 클릭하면 모든 검색 결과를 보여준다.")
    void findAllSearch() {
        // given
        Long expectId = 1L;
        List<SearchResponseDto> searchResponseDtos = List.of(
                SearchResponseDto.from(search),
                SearchResponseDto.from(search1)
        );
        SearchsResponseDto expected = SearchsResponseDto.from(searchResponseDtos);

        // when
        SearchsResponseDto actual = galleryService.findAllSearch(expectId);

        // then
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);
    }
}