package com.langlearnquiz.backend.services;

import com.langlearnquiz.backend.exceptions.EmptyImageException;
import com.langlearnquiz.backend.exceptions.EmptyImageFilenameException;
import com.langlearnquiz.backend.exceptions.NotAllowedFileExtensionException;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TextExtractionService {
    @Value("${extractor-service-url}")
    String extractorServiceUrl;

    @Value("${extractor-service-default-filename-key-name}")
    String extractorServiceDefaultKeyName;

    @Setter
    private RestTemplate restTemplate;

    private final List<String> ALLOWED_FILE_EXTENSIONS = List.of("png", "jpg", "jpeg");

    /**
     * Extract text content from an image using an external service.
     * <p>
     * This method sends an image file to an external service for text extraction. It constructs a
     * request with the image data and communicates with the external service via a POST request.
     *
     * @param image The image file from which text is to be extracted.
     * @return An optional containing the extracted text as a String, or an empty optional if the
     *         extraction process encounters an error or the response is empty.
     * @throws IOException If an I/O error occurs while processing the image or communicating with
     *                     the external service.
     */
    public Optional<String> extractTextFromImg(MultipartFile image) {
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

        String response = restTemplate.postForEntity(extractorServiceUrl, requestEntity, String.class).getBody();
        Optional<String> opt = Optional.ofNullable(response);
        return opt;

    }
}
