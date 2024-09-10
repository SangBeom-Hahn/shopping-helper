package com.sketch2fashion.backend.controller;


import com.sketch2fashion.backend.controller.dto.ClothesSaveRequest;
import com.sketch2fashion.backend.domain.file.FileMetaData;
import com.sketch2fashion.backend.service.ClothesService;
import com.sketch2fashion.backend.service.MessageService;
import com.sketch2fashion.backend.service.dto.ClothesSaveResponseDto;
import com.sketch2fashion.backend.support.FileConverter;
import com.sketch2fashion.backend.support.FileUploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/api/clothes")
@RequiredArgsConstructor
public class ClothesController {

    private final ClothesService clothesService;
    private final MessageService messageService;
    private final FileUploader fileUploader;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ClothesSaveResponseDto> upload(@ModelAttribute @Validated ClothesSaveRequest clothesSaveRequest) {

        FileMetaData fileMetaData = FileConverter.convertImage(clothesSaveRequest.getImage());
        String storeFilePath = fileUploader.upload(fileMetaData);
        Long saveMessageId = messageService.createMessage(storeFilePath)
                .getId();
        ClothesSaveResponseDto clothesSaveResponseDto =
                clothesService.createClothes(saveMessageId, storeFilePath, fileMetaData.getOriginalFileName());

        return ResponseEntity
                .created(URI.create("/api/clothes/upload/" + clothesSaveResponseDto.getId()))
                .body(clothesSaveResponseDto);
    }
}
