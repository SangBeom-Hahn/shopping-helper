package com.sketch2fashion.backend.support;

import com.sketch2fashion.backend.domain.file.FileExtension;
import com.sketch2fashion.backend.domain.file.FileMetaData;
import com.sketch2fashion.backend.exception.AbsentFileException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public class FileConverter {

    public static FileMetaData convertImage(MultipartFile image) {
        validateImageExist(image);

        try {
            String originalFilename = image.getOriginalFilename();

            return new FileMetaData(
                    image.getInputStream(),
                    originalFilename,
                    image.getContentType(),
                    FileExtension.from(originalFilename.toLowerCase())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void validateImageExist(MultipartFile image) {
        if(image == null || image.isEmpty()) {
            throw new AbsentFileException();
        }
    }
}
