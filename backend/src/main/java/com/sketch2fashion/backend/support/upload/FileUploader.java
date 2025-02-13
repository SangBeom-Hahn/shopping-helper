package com.sketch2fashion.backend.support.upload;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.sketch2fashion.backend.domain.file.FileMetaData;
import com.sketch2fashion.backend.exception.AbsentFileException;
import com.sketch2fashion.backend.support.UuidHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

import static com.sketch2fashion.backend.utils.SketchConstants.PATH;
import static com.sketch2fashion.backend.utils.SketchConstants.STORE_PATH_FORMAT;

@Component
public class FileUploader {

    private final String bucketName;

    private final Storage storage;

    private final UuidHolder uuidHolder;

    public FileUploader(@Value("${spring.cloud.gcp.storage.bucket}") String bucketName, Storage storage, UuidHolder uuidHolder) {
        this.bucketName = bucketName;
        this.storage = storage;
        this.uuidHolder = uuidHolder;
    }

    public String upload(final FileMetaData fileMetaData) {
        validateExistImage(fileMetaData);
        return sendImageToStorage(fileMetaData);
    }

    private String sendImageToStorage(final FileMetaData fileMetaData) {
        try (final InputStream file = fileMetaData.getRawFile()) {
            final String uuid = uuidHolder.createUuid();
            final String contentType = fileMetaData.getContentType();
            final String extension = fileMetaData.getExtension()
                    .getValues();
            final String storePath = String.format(STORE_PATH_FORMAT, PATH, uuid, extension);

            final BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, storePath)
                    .setContentType(contentType)
                    .build();

            storage.create(blobInfo, file);
            return storePath;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void validateExistImage(final FileMetaData fileMetaData) {
        if(fileMetaData == null) {
            throw new AbsentFileException();
        }
    }
}
