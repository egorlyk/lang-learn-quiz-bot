package com.langlearnquiz.backend.services;

import com.langlearnquiz.backend.exceptions.InvalidServiceURLException;
import com.langlearnquiz.backend.exceptions.image.EmptyImageException;
import com.langlearnquiz.backend.exceptions.image.EmptyImageFilenameException;
import com.langlearnquiz.backend.exceptions.image.NotAllowedFileExtensionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

@Service
public class TextExtractionService {
    @Value("${extractor-service-url}")
    String extractorServiceUrl;

    @Value("${extractor-service-default-filename-key-name}")
    String extractorServiceDefaultKeyName;

    @Autowired
    private RestTemplate restTemplate;

    private final List<String> ALLOWED_FILE_EXTENSIONS = List.of("png", "jpg", "jpeg");

    Logger log = LoggerFactory.getLogger(TextExtractionService.class);

    /**
     * Extract text content from an image using an external service.
     * <p>
     * This method sends an image file to an external service for text extraction. It constructs a
     * request with the image data and communicates with the external service via a POST request.
     *
     * @param image The image file from which text is to be extracted.
     * @return An optional containing the extracted text as a String, or an empty optional if the
     *         extraction process encounters an error or the response is empty.
     */
    public String extractTextFromImg(MultipartFile image) {
        return extractTextFromImg(image, extractorServiceUrl, extractorServiceDefaultKeyName, restTemplate);
    }

    /**
     * Extract text content from an image using an external service.
     * <p>
     * This method sends an image file to an external service for text extraction. It constructs a
     * request with the image data and communicates with the external service via a POST request.
     *
     * @param image The image file from which text is to be extracted.
     * @param extractorServiceUrl Extractor service url to send requests with {@link RestTemplate}
     * @param extractorServiceDefaultKeyName Extractor service key that used to accept the file on the service
     * @param restTemplate {@link RestTemplate} dependency to send requests
     *
     * @return An optional containing the extracted text as a String, or an empty optional if the
     *         extraction process encounters an error or the response is empty.
     */
    public String extractTextFromImg(MultipartFile image, String extractorServiceUrl,
                                     String extractorServiceDefaultKeyName, RestTemplate restTemplate) {
        if(image.isEmpty()) {
            throw new EmptyImageException("Image file is empty");
        }
        String imageFilename = image.getOriginalFilename();

        if(imageFilename == null || imageFilename.isEmpty()) {
            throw new EmptyImageFilenameException("Image filename is empty");
        }

        String[] filenameParts = imageFilename.split("\\.");
        String fileExtension = filenameParts[filenameParts.length - 1];

        if(!ALLOWED_FILE_EXTENSIONS.contains(fileExtension)){
            throw new NotAllowedFileExtensionException("File extension is not allowed. Use only .jpg, .jpeg or .png");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();

        // Specify the name for the key in the request to the server
        formData.add(extractorServiceDefaultKeyName, new HttpEntity<>(image.getResource()));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(formData, headers);

        try{
            URI serviceUri = new URL(extractorServiceUrl).toURI();
            String response = restTemplate.postForEntity(serviceUri, requestEntity, String.class).getBody();

            if(log.isDebugEnabled()) {
                log.debug("Text:\n" + getStringSubstringCut(response, 200));
                log.debug("Text length: " + response.length() + " symbols");
            }
            return response;
        } catch (MalformedURLException | URISyntaxException e) {
            throw new InvalidServiceURLException("Invalid Service URL");
        }
    }

    private String getStringSubstringCut(String string, int responseLength) {
        return string.length() <= responseLength ? string: string.substring(0, responseLength/2) +
                "\n...\n" +
                string.substring(string.length() - responseLength/2);
    }
}
