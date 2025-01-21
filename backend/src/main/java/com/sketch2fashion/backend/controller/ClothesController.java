package com.sketch2fashion.backend.controller;


import com.sketch2fashion.backend.controller.dto.ClothesSaveRequest;
import com.sketch2fashion.backend.controller.dto.ClothesSharedRequest;
import com.sketch2fashion.backend.controller.dto.ClothesUpdateRequest;
import com.sketch2fashion.backend.domain.file.BasicSketch;
import com.sketch2fashion.backend.domain.file.FileMetaData;
import com.sketch2fashion.backend.domain.message.ObjectType;
import com.sketch2fashion.backend.service.ClothesService;
import com.sketch2fashion.backend.service.MessageService;
import com.sketch2fashion.backend.service.ResultService;
import com.sketch2fashion.backend.service.dto.ClothesSaveResponseDto;
import com.sketch2fashion.backend.service.dto.ResultResponseDto;
import com.sketch2fashion.backend.support.FileConverter;
import com.sketch2fashion.backend.support.upload.FileUploader;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import static com.sketch2fashion.backend.utils.SketchConstants.ATTACH_FILENAME;
import static com.sketch2fashion.backend.utils.SketchConstants.BASIC_SKETCH_PATH;

@RestController
@RequestMapping("/api/clothes")
@RequiredArgsConstructor
public class ClothesController {

    private final ClothesService clothesService;
    private final MessageService messageService;
    private final FileUploader fileUploader;
    private final ResultService resultService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ClothesSaveResponseDto> upload(@ModelAttribute @Validated ClothesSaveRequest clothesSaveRequest) {

        final FileMetaData fileMetaData = FileConverter.convertImage(clothesSaveRequest.getImage());
        final String storeFilePath = fileUploader.upload(fileMetaData);
        final Long saveMessageId = messageService.createMessage(
                ObjectType.from(clothesSaveRequest.getObjectType()),
                storeFilePath,
                clothesSaveRequest.getRefine()
        ).getId();

        final ClothesSaveResponseDto clothesSaveResponseDto =
                clothesService.createClothes(saveMessageId, storeFilePath, fileMetaData.getOriginalFileName());

        return ResponseEntity
                .created(URI.create("/api/clothes/upload/" + clothesSaveResponseDto.getId()))
                .body(clothesSaveResponseDto);
    }

    @GetMapping("/{messageId}")
    public ResponseEntity<ResultResponseDto> findInferenceResult(@PathVariable("messageId") Long messageId) {
        return ResponseEntity.ok(resultService.findResult(messageId));
    }

    @PutMapping("/{messageId}")
    public ResponseEntity<Void> updateRate(
            @PathVariable("messageId") Long messageId,
            @RequestBody @Validated ClothesUpdateRequest clothesUpdateRequest
    ) {
        resultService.updateResult(messageId, clothesUpdateRequest.getRating(), clothesUpdateRequest.getReview());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{messageId}/share")
    public ResponseEntity<Void> share(
            @PathVariable("messageId") Long messageId,
            @RequestBody @Validated ClothesSharedRequest clothesSharedRequest
    ) {
        resultService.updateShared(messageId, clothesSharedRequest.getShared());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/images/{imageId}")
    public Resource downloadImage(@PathVariable("imageId") String imageId) throws IOException {
        return new UrlResource(String.format(BASIC_SKETCH_PATH, imageId));
    }

    @GetMapping("/download/{imageId}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable("imageId") String imageId) throws IOException {
        final UrlResource urlResource = new UrlResource(String.format(BASIC_SKETCH_PATH, imageId));
        String encodedFileName = UriUtils.encode(
                BasicSketch.from(imageId).getName(),
                StandardCharsets.UTF_8
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, String.format(ATTACH_FILENAME, encodedFileName))
                .body(urlResource);
    }
}
