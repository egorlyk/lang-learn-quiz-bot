package com.langlearnquiz.backend.services;

import com.langlearnquiz.backend.exceptions.EmptyImageException;
import com.langlearnquiz.backend.exceptions.EmptyImageFilenameException;
import com.langlearnquiz.backend.exceptions.NotAllowedFileExtensionException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class TextExtractionServiceTest {
    private AutoCloseable autoCloseable;
    @Mock
    private RestTemplate restTemplate;

    private final TextExtractionService teService = new TextExtractionService();

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        restTemplate = Mockito.mock(RestTemplate.class);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void emptyImageShouldThrowException() {
        MockMultipartFile mockFile = new MockMultipartFile("file", new byte[]{});
        assertThatThrownBy(() -> teService.extractTextFromImg(mockFile)).isInstanceOf(EmptyImageException.class);
    }

    @Test
    void emptyImageFilenameShouldThrowException() {
        byte[] fileContent = "Mock file content".getBytes();
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",        // Name of the file input field
                null, // Original file name
                "text/plain",   // Content type
                fileContent     // File content as a byte array
        );
         assertThatThrownBy(() -> teService.extractTextFromImg(mockFile)).isInstanceOf(EmptyImageFilenameException.class);
    }

    @Test
    void notAllowedImageFilenameShouldThrowException() {
        byte[] fileContent = "Mock file content".getBytes();
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",        // Name of the file input field
                "file.txt", // Original file name
                "text/plain",   // Content type
                fileContent     // File content as a byte array
        );
        assertThatThrownBy(() -> teService.extractTextFromImg(mockFile)).isInstanceOf(NotAllowedFileExtensionException.class);
    }

    @Test
    void properImageShouldReturnSpecificText() {
        byte[] fileContent = "Mock file content".getBytes();
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "file.png",
                "text/plain",
                fileContent
        );

        String expectedResponse = "Extracted text";

        Mockito.when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        final String EXTRACTOR_SERVICE_KEY_NAME = "ext-img";
        final String EXTRACTOR_SERVICE_URL = "some-url";

        assertThat(teService.extractTextFromImg(mockFile, EXTRACTOR_SERVICE_KEY_NAME, EXTRACTOR_SERVICE_URL,
                restTemplate).get()).isEqualTo(expectedResponse);
    }
}