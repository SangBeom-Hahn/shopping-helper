package com.sketch2fashion.backend.document;

import com.sketch2fashion.backend.controller.ClothesController;
import com.sketch2fashion.backend.controller.dto.ClothesUpdateRequest;
import com.sketch2fashion.backend.domain.file.FileMetaData;
import com.sketch2fashion.backend.domain.message.ObjectType;
import com.sketch2fashion.backend.service.dto.ClothesSaveResponseDto;
import com.sketch2fashion.backend.service.dto.MessageSaveResponseDto;
import com.sketch2fashion.backend.service.dto.ResultResponseDto;
import com.sketch2fashion.backend.support.consume.dto.InferencesResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ClothesController.class)
class ClothesControllerTest extends ControllerTest {

    @Test
    @DisplayName("/upload API를 호출하면 201 create로 응답한다.")
    void upload() throws Exception {
        // given
        String fileName = "test.jpeg";
        InputStream fileInputStream = new FileInputStream("src/test/resources/" + fileName);
        MockMultipartFile file = new MockMultipartFile("image", fileName, "image/jpeg", fileInputStream);

        given(fileUploader.upload(any(FileMetaData.class)))
                .willReturn("PATH");
        given(messageService.createMessage(any(ObjectType.class), anyString(), anyBoolean()))
                .willReturn(MessageSaveResponseDto.from(1L));
        given(clothesService.createClothes(anyLong(), anyString(), anyString()))
                .willReturn(ClothesSaveResponseDto.from(1L));

        // when // then
        mockMvc.perform(
                    multipart("/api/clothes/upload")
                            .file(file)
                            .param("objectType", "T_SHIRTS")
                            .param("refine", "false")
                            .contentType(MULTIPART_FORM_DATA)
                )
                .andExpect(status().isCreated())
                .andDo(restDocs.document())
                ;
    }

    @Test
    @DisplayName("/{messageId} GET 요청을 하면 200 OK로 응답한다.")
    void findInferenceResult() throws Exception {
        // given
        Long expectedId = 1L;
        InferencesResponse inferencesResponse =
                InferencesResponse.of(0, 0, 0, 0, 0, 0, 0, List.of());
        given(resultService.findResult(anyLong()))
                .willReturn(ResultResponseDto.of(inferencesResponse, "PATH", "PATH"));

        // when // then
        mockMvc.perform(
                        get("/api/clothes/{messageId}", expectedId)
                )
                .andExpect(status().isOk())
                .andDo(restDocs.document())
                ;
    }

    @Test
    @DisplayName("/{messageId} PUT 요청을 하면 204 NO CONTENT로 응답한다.")
    void updateRate() throws Exception {
        // given
        Long expectedId = 1L;
        ClothesUpdateRequest clothesUpdateRequest = new ClothesUpdateRequest(1, "REVIEW");
        willDoNothing().given(resultService)
                .updateResult(anyLong(), anyInt(), anyString());

        // when // then
        mockMvc.perform(
                        put("/api/clothes/{messageId}", expectedId)
                                .content(objectMapper.writeValueAsString(clothesUpdateRequest))
                                .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andDo(restDocs.document())
                ;
    }
}