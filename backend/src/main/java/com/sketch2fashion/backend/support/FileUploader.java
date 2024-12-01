package com.sketch2fashion.backend.support;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.sketch2fashion.backend.domain.file.FileExtension;
import com.sketch2fashion.backend.domain.file.FileMetaData;
import com.sketch2fashion.backend.exception.AbsentFileException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Component
public class FileUploader {

    private final String bucketName;

    private final Storage storage;

    public FileUploader(@Value("${spring.cloud.gcp.storage.bucket}") String bucketName, Storage storage) {
        this.bucketName = bucketName;
        this.storage = storage;
    }

    public String upload(FileMetaData fileMetaData) {
        validateExistImage(fileMetaData);
        return sendImageToStorage(fileMetaData);
    }

    private String sendImageToStorage(FileMetaData fileMetaData) {
        try (InputStream file = fileMetaData.getRawFile()) {
            String uuid = UUID.randomUUID().toString();
            String contentType = fileMetaData.getContentType();
            String extension = fileMetaData.getExtension()
                    .getValues();
            String storePath = "sketch2fashion/upload/" + uuid + extension;

            BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, storePath)
                    .setContentType(contentType)
                    .build();

            storage.create(blobInfo, file);
            return String.format("%s/%s/%s", "https://storage.googleapis.com", bucketName, storePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void validateExistImage(FileMetaData fileMetaData) {
        if(fileMetaData == null) {
            throw new AbsentFileException();
        }
    }
}
