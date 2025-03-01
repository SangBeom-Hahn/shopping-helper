package com.sketch2fashion.backend.service;

import com.sketch2fashion.backend.domain.file.FileMetaData;
import com.sketch2fashion.backend.domain.message.Message;
import com.sketch2fashion.backend.domain.message.ObjectType;
import com.sketch2fashion.backend.exception.NoSuchMessageException;
import com.sketch2fashion.backend.service.dto.ClothesResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.sketch2fashion.backend.domain.file.FileExtension.JPEG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

class ClothesServiceTest extends ServiceTest {

    private Message message;

    @BeforeEach
    void setUp() {
        message = new Message(ObjectType.T_SHIRTS, "path", false);

        messageRepository.save(message);
    }

    @Test
    @DisplayName("존재하지 않는 메세지로 이미지 메타 데이터를 저장하면 예외가 발생한다.")
    void throwException_noSuchMessage() {
        // given
        Long invalidMessageId = 3L;

        // then
        assertThatThrownBy(() -> clothesService.createClothes(invalidMessageId, "path", "name"))
                .isInstanceOf(NoSuchMessageException.class);
    }

    @Test
    @DisplayName("디자인 이미지를 업로드하여 메타 데이터를 저장하고 찾는다.")
    void createClothesAndFind() throws IOException {
        // given
        String URL = "https://storage.googleapis.com/test/1234abcd1234abcd.mp4";
        String originalFileName = "test.jpeg";

        InputStream fileInputStream = new FileInputStream("src/test/resources/" + originalFileName);
        MockMultipartFile mockFile = new MockMultipartFile("test_mock", originalFileName, "jpeg", fileInputStream);
        FileMetaData fileMetaData = new FileMetaData(mockFile.getInputStream(), "test", "jpeg", JPEG);

        // when
        when(fakeUploader.upload(fileMetaData))
                .thenReturn(URL);
        Long saveId = clothesService.createClothes(
                message.getId(),
                fakeUploader.upload(fileMetaData),
                fileMetaData.getOriginalFileName()
        ).getId();
        ClothesResponseDto clothesResponseDto = clothesService.findClothes(message.getId());

        // then
        assertThat(clothesResponseDto).extracting("id", "messageId", "uploadFileName", "storeFilePath")
                .containsExactly(
                        saveId,
                        message.getId(),
                        fileMetaData.getOriginalFileName(),
                        URL
                );
    }
}
