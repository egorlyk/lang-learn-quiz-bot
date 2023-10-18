package com.langlearnquiz.frontend.utils;

import com.langlearnquiz.frontend.dtos.CallbackAnswerDTO;
import com.langlearnquiz.frontend.dtos.QuestionDTO;
import com.langlearnquiz.frontend.dtos.UserDTO;
import com.langlearnquiz.frontend.exceptions.EmptyUserException;
import com.langlearnquiz.frontend.exceptions.InvalidServiceURLException;
import com.langlearnquiz.frontend.exceptions.NullAnswerServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * {@link RestServiceUtil} class created for frontend-backend communication. Contains specific methods for entities
 * transfer
 */
@Component
public class RestServiceUtil {
    Logger log = LoggerFactory.getLogger(RestServiceUtil.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${TG_BOT_API}")
    private String telegramBotAPI;

    @Value("${backend-service-user-url}")
    private String backendServiceUserURL;

    @Value("${backend-service-check-answer-url}")
    private String backendServiceCheckAnswerURL;

    @Value("${backend-service-process-key-name}")
    private String backendServiceProcessKeyName;

    @Value("${backend-service-process-url}")
    private String backendServiceProcessURL;

    @Value("${backend-service-generate-with-topic-url}")
    private String backendServiceGenerateWithTopicURL;

    /**
     * Return used from backend database by chatId
     *
     * @param chatId Bot-user chat ID
     * @return {@link UserDTO} user from backend database
     */
    public UserDTO getUser(long chatId) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(backendServiceUserURL)
                .path("/{id}");

        // Build the final URL with query parameters
        String finalUrl = builder.build(chatId).toString();

        URI serviceUri;
        try {
            serviceUri = new URL(finalUrl).toURI();
        } catch (URISyntaxException | MalformedURLException e) {
            throw new InvalidServiceURLException(e.getMessage());
        }

        ResponseEntity<UserDTO> responseEntity = restTemplate.getForEntity(serviceUri, UserDTO.class);
        UserDTO body = responseEntity.getBody();
        if (body == null) {
            throw new NullAnswerServiceException("Answer from service is null");
        }

        if (log.isDebugEnabled()) {
            log.debug("Frontend user by id response" + body);
        }

        return body;
    }

    /**
     * Save/update user to backend database
     *
     * @param userDTO {@link UserDTO} user
     */
    public void saveUser(UserDTO userDTO) {
        if (userDTO == null || userDTO.getChatId() == 0) {
            throw new EmptyUserException("User can't be empty or null");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        URI serviceUri;
        try {
            serviceUri = new URL(backendServiceUserURL).toURI();
        } catch (URISyntaxException | MalformedURLException e) {
            throw new InvalidServiceURLException(e.getMessage());
        }

        HttpEntity<UserDTO> requestEntity = new HttpEntity<>(userDTO, headers);
        restTemplate.postForEntity(serviceUri, requestEntity, Void.class);
    }

    /**
     * Gives an answer and a reason whether the user answered the quiz correctly
     *
     * @param chatId            Bot-user chat ID
     * @param chosenAnswerIndex Response index chosen by user
     * @return Response from server with correct answer and reason why answer was chosen
     */
    public String getCorrectAnswerResponse(long chatId, int chosenAnswerIndex) {
        CallbackAnswerDTO callbackAnswerDTO = new CallbackAnswerDTO(chatId, chosenAnswerIndex);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(backendServiceCheckAnswerURL)
                .queryParam("chatId", callbackAnswerDTO.getChatId()) // Add query parameters as needed
                .queryParam("answerIndex", callbackAnswerDTO.getChoseAnswerIndex());

        // Build the final URL with query parameters
        String finalUrl = builder.toUriString();

        URI serviceUri;
        try {
            serviceUri = new URL(finalUrl).toURI();
        } catch (URISyntaxException | MalformedURLException e) {
            throw new InvalidServiceURLException(e.getMessage());
        }

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(serviceUri, String.class);

        String body = responseEntity.getBody();
        if (body == null) {
            throw new NullAnswerServiceException("Answer from service is null");
        }

        if (log.isDebugEnabled()) {
            log.debug("Frontend correct answer response" + body);
        }

        return body;
    }

    /**
     * Downloads the file from Telegram file storage server
     *
     * @param filePath File path
     * @return {@link ByteArrayResource} file as an array of bytes
     */
    public ByteArrayResource getFileFromTelegramServerByFilePath(String filePath) {
        String photoUrl = String.format("https://api.telegram.org/file/bot%s/%s", telegramBotAPI, filePath);
        final String DOWNLOAD_FILENAME = "file.png";

        URI serviceUri;
        try {
            serviceUri = new URL(photoUrl).toURI();
        } catch (URISyntaxException | MalformedURLException e) {
            throw new InvalidServiceURLException(e.getMessage());
        }

        byte[] fileResponse = restTemplate.
                getForEntity(serviceUri, byte[].class).getBody();

        if (fileResponse == null) {
            throw new NullAnswerServiceException("Answer from service is null");
        }

        return new ByteArrayResource(fileResponse) {
            @Override
            public String getFilename() {
                return DOWNLOAD_FILENAME;
            }
        };
    }

    /**
     * Get topic from image sent by user
     *
     * @param chatId Bot-user chat ID
     * @param file   {@link ByteArrayResource} file in bytes format
     * @return topic
     */
    public String getTopicFromParsedImage(long chatId, ByteArrayResource file) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add("chat_id", String.valueOf(chatId));

        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();

        formData.add(backendServiceProcessKeyName, file);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(formData, headers);

        URI serviceUri;
        try {
            serviceUri = new URL(backendServiceProcessURL).toURI();
        } catch (URISyntaxException | MalformedURLException e) {
            throw new InvalidServiceURLException(e.getMessage());
        }

        String body = restTemplate.
                postForEntity(serviceUri, requestEntity, String.class).getBody();


        if (body == null) {
            throw new NullAnswerServiceException("Answer from service is null");
        }

        if (log.isDebugEnabled()) {
            log.debug("Frontend topic response" + body);
        }

        return body;
    }

    /**
     * Get a question with defined topic.
     *
     * @param topic Topic to get a question
     * @return {@link QuestionDTO} question
     */
    public QuestionDTO getQuestionWithTopic(String topic) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(backendServiceGenerateWithTopicURL)
                .queryParam("topic", topic);

        // Build the final URL with query parameters
        String finalUrl = builder.toUriString();

        URI serviceUri;
        try {
            serviceUri = new URL(finalUrl).toURI();
        } catch (URISyntaxException | MalformedURLException e) {
            throw new InvalidServiceURLException(e.getMessage());
        }

        ResponseEntity<QuestionDTO> responseEntity = restTemplate.getForEntity(serviceUri, QuestionDTO.class);
        QuestionDTO body = responseEntity.getBody();

        if (body == null) {
            throw new NullAnswerServiceException("Answer from service is null");
        }

        if (log.isDebugEnabled()) {
            log.debug("Frontend question response: " + body);
        }

        return body;
    }
}
