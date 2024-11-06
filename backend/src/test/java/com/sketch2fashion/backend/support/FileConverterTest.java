package com.sketch2fashion.backend.support;

import com.sketch2fashion.backend.domain.file.FileExtension;
import com.sketch2fashion.backend.domain.file.FileMetaData;
import com.sketch2fashion.backend.exception.AbsentFileException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.sketch2fashion.backend.domain.file.FileExtension.JPEG;
import static org.junit.jupiter.api.Assertions.*;

class FileConverterTest {

    @Test
    @DisplayName("빈 파일을 변환하려고 하면 예외가 발생한다.")
    void throwException_notExistImage() {
        // given
        MockMultipartFile fakeFile = null;

        // then
        Assertions.assertThatThrownBy(() -> FileConverter.convertImage(fakeFile))
                .isInstanceOf(AbsentFileException.class);
    }
    
    @Test
    @DisplayName("업로드 파일에서 메타 데이터를 추출한다.")
    void convertImage() throws IOException {
        // given
        InputStream rawData = new FileInputStream("src/test/resources/test.jpeg");
        MockMultipartFile fakeFile = new MockMultipartFile("test.jpeg", "test.jpeg", "image/jpeg", rawData);

        // when
        FileMetaData fileMetaData = FileConverter.convertImage(fakeFile);

        // then
        Assertions.assertThat(fileMetaData).extracting("originalFileName", "contentType", "extension")
                .containsExactly("test.jpeg", "image/jpeg", JPEG);
    }
}