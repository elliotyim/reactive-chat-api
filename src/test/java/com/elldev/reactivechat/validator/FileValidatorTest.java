package com.elldev.reactivechat.validator;

import com.elldev.reactivechat.constant.FileSize;
import com.elldev.reactivechat.exception.BadRequestException;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class FileValidatorTest {
    @Test
    void Should_ProfileType_Image() throws BadRequestException {
        // given
        String name = "profileImg";
        String originalFileName = "test.json";
        String contentType = MediaType.APPLICATION_JSON_VALUE;
        byte[] content = "{\"key: \"value\"}".getBytes(StandardCharsets.UTF_8);

        // when
        MockMultipartFile jsonFile = new MockMultipartFile(
                name,
                originalFileName,
                contentType,
                content);

        // then
        assertThrows(BadRequestException.class,
                () -> FileValidator.checkProfileImg(jsonFile)
        );
    }

    @Test
    void Should_Profile_Not_Exceed_10MB() throws BadRequestException {
        // given
        String name = "profileImg";
        String originalFileName = "test.json";
        String contentType = MediaType.IMAGE_JPEG_VALUE;
        byte[] content = new byte[10 * 1024 * 1024 + 1];

        // when
        MockMultipartFile imgFile = new MockMultipartFile(
                name,
                originalFileName,
                contentType,
                content);

        // then
        assertThrows(BadRequestException.class,
                () -> FileValidator.checkProfileImg(imgFile)
        );
    }
}
