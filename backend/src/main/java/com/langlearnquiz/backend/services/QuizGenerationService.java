package com.langlearnquiz.backend.services;

import com.langlearnquiz.backend.dto.QuestionDTO;
import com.langlearnquiz.backend.exceptions.text.EmptyTopicException;
import com.langlearnquiz.backend.exceptions.InvalidServiceURLException;
import com.langlearnquiz.backend.exceptions.text.EmptyTextException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    @Value("${generator-service-generate-default-key-name}")
    String generatorServiceGenerateDefaultKeyName;

    @Value("${generator-service-topic-default-key-name}")
    String generatorServiceTopicDefaultKeyName;

    @Value("${generator-service-topic-url}")
    String generatorServiceTopicUrl;

    @Value("${generator-service-generate-url}")
    String generatorServiceGenerateUrl;

    @Autowired
    RestTemplate restTemplate;

    Logger log = LoggerFactory.getLogger(QuizGenerationService.class);

    /**
     * Generates a quiz based on the provided text by making a POST request to a generator service.
     *
     * @param text The input text used for generating quiz questions.
     * @return An optional containing a {@link QuestionDTO} object representing quiz question and answers,
     *         or an empty optional if an error occurs during the request.
     * @throws RestClientException If an error occurs while making the REST API request.
     */
    public QuestionDTO generateQuiz(String text){
        return generateQuiz(text, generatorServiceGenerateUrl, generatorServiceGenerateDefaultKeyName, restTemplate);
    }

    /**
     * Generates a quiz based on the provided text by making a POST request to a generator service.
     *
     * @param topic The input topic used for generating quiz question.
     * @param generatorServiceUrl Generator service url to send requests with {@link RestTemplate}
     * @param generatorServiceMessageKeyName Generator service key that used to accept the topic on the service
     * @param restTemplate {@link RestTemplate} dependency to send requests
     *
     * @return An optional containing a {@link QuestionDTO} object representing quiz question and answers,
     *         or an empty optional if an error occurs during the request.
     * @throws RestClientException If an error occurs while making the REST API request.
     */
    public QuestionDTO generateQuiz(String topic, String generatorServiceUrl,
                                          String generatorServiceMessageKeyName, RestTemplate restTemplate) {
        if(topic.isEmpty()){
            throw new EmptyTopicException("Topic can't be empty");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        formData.add(generatorServiceMessageKeyName, topic);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(formData, headers);

        try {
            URI serviceUri = new URL(generatorServiceUrl).toURI();
            ResponseEntity<QuestionDTO> response =
                    restTemplate.postForEntity(serviceUri, requestEntity, QuestionDTO.class);
            QuestionDTO responseBody = response.getBody();

            if(log.isDebugEnabled()) {
                log.debug("Generated question: " + responseBody);
            }

            return responseBody;
        }
        catch (MalformedURLException | URISyntaxException e) {
            throw new InvalidServiceURLException("Invalid Service URL");
        }
    }

    public String getTopic(String text) {
        if (text.isEmpty()) {
            throw new EmptyTextException("Text can't be empty");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        formData.add(generatorServiceTopicDefaultKeyName, text);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(formData, headers);

        try {
            URI serviceUri = new URL(generatorServiceTopicUrl).toURI();
            ResponseEntity<String> response =
                    restTemplate.postForEntity(serviceUri, requestEntity, String.class);
            String responseBody = response.getBody();

            if (log.isDebugEnabled()) {
                log.debug(responseBody);
            }
            return responseBody;
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
