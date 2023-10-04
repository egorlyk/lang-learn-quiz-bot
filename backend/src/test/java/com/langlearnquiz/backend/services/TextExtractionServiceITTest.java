package com.langlearnquiz.backend.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TextExtractionServiceITTest {

    @Mock
    RestTemplate restTemplate;

    @Autowired
    @InjectMocks
    TextExtractionService teService;

    @Test
    void properImageShouldReturnProperText(){
        String expectedResponse = "Response text";

        byte[] fileContent = "Mock file content".getBytes();
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "file.png",
                "text/plain",
                fileContent
        );

        when(restTemplate.postForEntity(any(URI.class), any(HttpEntity.class), eq(String.class)))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        assertThat(teService.extractTextFromImg(mockFile)).isEqualTo(expectedResponse);
    }
}
