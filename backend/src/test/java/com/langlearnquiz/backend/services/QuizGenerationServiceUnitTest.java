package com.langlearnquiz.backend.services;

import com.langlearnquiz.backend.dto.QuestionDTO;
import com.langlearnquiz.backend.exceptions.InvalidServiceURLException;
import com.langlearnquiz.backend.exceptions.text.EmptyTextException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuizGenerationServiceUnitTest {
    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    QuizGenerationService qgService;

    @Test
    void emptyTextShouldThrowException() {
        String text = "";
        String generatorServiceURL = "";
        String generatorKeyServiceURL = "";

        assertThatThrownBy(() -> qgService.generateQuiz(text, generatorServiceURL, generatorKeyServiceURL, restTemplate))
                .isInstanceOf(EmptyTextException.class);
    }

    @Test
    void invalidServiceURLShouldThrowException() {
        String text = "Some text";
        String generatorServiceURL = "";
        String generatorKeyServiceURL = "";

        assertThatThrownBy(() -> qgService.generateQuiz(text, generatorServiceURL, generatorKeyServiceURL, restTemplate))
                .isInstanceOf(InvalidServiceURLException.class);
    }

    @Test
    void properTextShouldReturnProperQuizResponse() throws MalformedURLException, URISyntaxException {
        String text = "Some text to the service";
        String generatorServiceURL = "http://localhost:5050/";
        URI generatorServiceURI = new URL(generatorServiceURL).toURI();
        QuestionDTO expectedResponse = new QuestionDTO("Some question",
                List.of("a1", "a2", "a3"), 1, "Valuable reason");

        when(restTemplate.postForEntity(eq(generatorServiceURI), any(HttpEntity.class), eq(QuestionDTO.class)))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        assertThat(qgService.generateQuiz(text, generatorServiceURL, "", restTemplate))
                .isEqualTo(expectedResponse);
    }
}