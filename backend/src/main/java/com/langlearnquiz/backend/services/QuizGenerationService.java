package com.langlearnquiz.backend.services;

import com.langlearnquiz.backend.dtos.QuestionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class QuizGenerationService {
    @Value("${generator-service-default-message-key-name}")
    String generatorServiceMessageKeyName;

    @Value("${generator-service-url}")
    String generatorServiceUrl;
    @Autowired
    RestTemplate restTemplate;

    /**
     * Generates a quiz based on the provided text by making a POST request to a generator service.
     *
     * @param text The input text used for generating quiz questions.
     * @return An optional containing a list of {@link QuestionDTO} objects representing quiz questions and answers,
     *         or an empty optional if an error occurs during the request.
     * @throws RestClientException If an error occurs while making the REST API request.
     */
    public Optional<List<QuestionDTO>> generateQuiz(String text){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        formData.add(generatorServiceMessageKeyName, text);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(formData, headers);
        try {
            ResponseEntity<List<QuestionDTO>> response = restTemplate.exchange(
                    generatorServiceUrl,
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<>() {
                    });

            return Optional.ofNullable(response.getBody());
        } catch (RestClientException e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }
}
