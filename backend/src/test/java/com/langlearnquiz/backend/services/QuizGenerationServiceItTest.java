package com.langlearnquiz.backend.services;

import com.langlearnquiz.backend.dtos.QuestionDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class QuizGenerationServiceItTest {
    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    @Autowired
    QuizGenerationService qgService;

    @Test
    void properTextShouldReturnProperAnser() {
        String inputText = "Some text";
        QuestionDTO question = new QuestionDTO("Some question",
                List.of("a1", "a2", "a3"), 1, "Valuable reason");
        List<QuestionDTO> expectedResponse = List.of(question);


        when(restTemplate.exchange(any(URI.class), eq(HttpMethod.POST), any(HttpEntity.class),
                eq(new ParameterizedTypeReference<List<QuestionDTO>>() {})))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        assertThat(qgService.generateQuiz(inputText)).isEqualTo(expectedResponse);
    }
}
