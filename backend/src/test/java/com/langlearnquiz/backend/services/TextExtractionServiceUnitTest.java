package com.langlearnquiz.backend.services;

import com.langlearnquiz.backend.exceptions.InvalidServiceURLException;
import com.langlearnquiz.backend.exceptions.image.EmptyImageException;
import com.langlearnquiz.backend.exceptions.image.EmptyImageFilenameException;
import com.langlearnquiz.backend.exceptions.image.NotAllowedFileExtensionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class TextExtractionServiceUnitTest {
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private final TextExtractionService teService = new TextExtractionService();

    @Test
    void emptyImageShouldThrowException() {
        MockMultipartFile mockFile = new MockMultipartFile("file", new byte[]{});
        String extractorServiceUrl = "";
        String extractorServiceDefaultKeyName = "";
        assertThatThrownBy(() -> teService.extractTextFromImg(mockFile, extractorServiceUrl,
                extractorServiceDefaultKeyName, restTemplate)).isInstanceOf(EmptyImageException.class);
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
        String extractorServiceUrl = "";
        String extractorServiceDefaultKeyName = "";
        assertThatThrownBy(() -> teService.extractTextFromImg(mockFile, extractorServiceUrl,
                extractorServiceDefaultKeyName, restTemplate)).isInstanceOf(EmptyImageFilenameException.class);
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
        String extractorServiceUrl = "";
        String extractorServiceDefaultKeyName = "";
        assertThatThrownBy(() -> teService.extractTextFromImg(mockFile, extractorServiceUrl,
                extractorServiceDefaultKeyName, restTemplate)).isInstanceOf(NotAllowedFileExtensionException.class);
    }

    @Test
    void invalidServiceURLShouldThrowException() {
        byte[] fileContent = "Mock file content".getBytes();
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",        // Name of the file input field
                "file.png", // Original file name
                "text/plain",   // Content type
                fileContent     // File content as a byte array
        );
        String extractorServiceUrl = "some";
        String extractorServiceDefaultKeyName = "";
        assertThatThrownBy(() -> teService.extractTextFromImg(mockFile, extractorServiceUrl,
                extractorServiceDefaultKeyName, restTemplate)).isInstanceOf(InvalidServiceURLException.class);
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

        Mockito.when(restTemplate.postForEntity(any(URI.class), any(HttpEntity.class), eq(String.class)))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        final String EXTRACTOR_SERVICE_KEY_NAME = "ext-img";
        final String EXTRACTOR_SERVICE_URL = "http://localhost:5000/extract";

        assertThat(teService.extractTextFromImg(mockFile, EXTRACTOR_SERVICE_URL, EXTRACTOR_SERVICE_KEY_NAME,
                restTemplate)).isEqualTo(expectedResponse);
    }
}