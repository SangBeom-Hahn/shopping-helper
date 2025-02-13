package com.sketch2fashion.backend.domain.file;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.InputStream;

@Getter
@AllArgsConstructor
public class FileMetaData {

    private InputStream rawFile;
    private String originalFileName;
    private String contentType;
    private FileExtension extension;
}
