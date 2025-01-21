package com.sketch2fashion.backend.document;

import com.sketch2fashion.backend.controller.GalleryController;
import com.sketch2fashion.backend.service.dto.GalleryListResponseDto;
import com.sketch2fashion.backend.service.dto.GallerysResponseDto;
import com.sketch2fashion.backend.service.dto.SearchResponseDto;
import com.sketch2fashion.backend.service.dto.SearchsResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = GalleryController.class)
class GalleryControllerTest extends ControllerTest {

    @Test
    @DisplayName("GET 요청을 보내면 200 OK로 응답한다.")
    void findGallery() throws Exception {
        // given
        List<GalleryListResponseDto> responseDtos = List.of();
        given(galleryService.findAllGallery())
                .willReturn(GallerysResponseDto.from(responseDtos));

        // when // then
        mockMvc.perform(
                        get("/api/gallery")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("/{resultId} API를 호출하면 200 OK 응답을 한다.")
    void findSearchs() throws Exception {
        // given
        Long expectedId = 1L;
        List<SearchResponseDto> responseDtos = List.of();
        given(galleryService.findAllSearch(anyLong()))
                .willReturn(SearchsResponseDto.from(responseDtos));

        // when // then
        mockMvc.perform(
                        get("/api/gallery/{resultId}", expectedId)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }
}