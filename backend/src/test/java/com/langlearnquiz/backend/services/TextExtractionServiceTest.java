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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {
        "extractor-service-url=http://localhost:5000/extract",
        "extractor-service-default-filename-key-name=ext-img"
})
class TextExtractionServiceTest {
    private AutoCloseable autoCloseable;
    @Mock
    private RestTemplate restTemplate;

    @Autowired
    private TextExtractionService teService;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        restTemplate = Mockito.mock(RestTemplate.class);
        teService.setRestTemplate(restTemplate);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Value("${extractor-service-url}")
    String extractorServiceUrl;

    @Value("${extractor-service-default-filename-key-name}")
    String extractorServiceDefaultKeyName;

    @Test
    void emptyImageShouldThrowErrorTest() {
        MockMultipartFile mockFile = new MockMultipartFile("file", new byte[]{});
        assertThatThrownBy(() -> teService.extractTextFromImg(mockFile)).isInstanceOf(EmptyImageException.class);
    }

    @Test
    void emptyImageFilenameShouldThrowErrorTest() {
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
    void notAllowedImageFilenameShouldThrowErrorTest() {
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
    void properImageShouldReturnTextTest() {
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

        assertThat(teService.extractTextFromImg(mockFile).get()).isEqualTo(expectedResponse);
    }
}