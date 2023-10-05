package com.langlearnquiz.backend.services;

import com.langlearnquiz.backend.dtos.QuestionDTO;
import com.langlearnquiz.backend.exceptions.InvalidServiceURLException;
import com.langlearnquiz.backend.exceptions.text.EmptyTextException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

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
     * @return An optional containing a {@link QuestionDTO} object representing quiz question and answers,
     *         or an empty optional if an error occurs during the request.
     * @throws RestClientException If an error occurs while making the REST API request.
     */
    public QuestionDTO generateQuiz(String text){
        return generateQuiz(text, generatorServiceUrl, generatorServiceMessageKeyName, restTemplate);
    }

    /**
     * Generates a quiz based on the provided text by making a POST request to a generator service.
     *
     * @param text The input text used for generating quiz question.
     * @param generatorServiceUrl Generator service url to send requests with {@link RestTemplate}
     * @param generatorServiceMessageKeyName Generator service key that used to accept the text on the service
     * @param restTemplate {@link RestTemplate} dependency to send requests
     *
     * @return An optional containing a {@link QuestionDTO} object representing quiz question and answers,
     *         or an empty optional if an error occurs during the request.
     * @throws RestClientException If an error occurs while making the REST API request.
     */
    public QuestionDTO generateQuiz(String text, String generatorServiceUrl,
                                          String generatorServiceMessageKeyName, RestTemplate restTemplate) {

        if(text.isEmpty()){
            throw new EmptyTextException("Text can't be empty");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        formData.add(generatorServiceMessageKeyName, text);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(formData, headers);

        try {
            URI serviceUri = new URL(generatorServiceUrl).toURI();

            ResponseEntity<QuestionDTO> response =
                    restTemplate.postForEntity(serviceUri, requestEntity, QuestionDTO.class);

            return response.getBody();
        }
        catch (MalformedURLException | URISyntaxException e) {
            throw new InvalidServiceURLException("Invalid Service URL");
        }
    }
}
