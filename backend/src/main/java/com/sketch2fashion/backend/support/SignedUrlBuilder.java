package com.sketch2fashion.backend.support;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class SignedUrlBuilder {

    private Storage storage;
    private String bucketName;

    public SignedUrlBuilder(
            Storage storage,
            @Value("${spring.cloud.gcp.storage.bucket}") String bucketName
    ) {
        this.storage = storage;
        this.bucketName = bucketName;
    }

    public String generateSignedUrl(String storeFilePath) {
        BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(bucketName, storeFilePath))
                .build();

        URL url = storage.signUrl(
                blobInfo,
                5,
                TimeUnit.MINUTES,
                Storage.SignUrlOption.withV4Signature()
        );
        return url.toString();
    }
}
