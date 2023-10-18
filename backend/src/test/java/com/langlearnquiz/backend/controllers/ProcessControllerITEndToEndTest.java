package com.langlearnquiz.backend.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.langlearnquiz.backend.dto.QuestionDTO;
import com.langlearnquiz.backend.services.QuizGenerationService;
import com.langlearnquiz.backend.services.TextExtractionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ProcessController.class)
class ProcessControllerITEndToEndTest {

    @MockBean
    QuizGenerationService qgService;

    @MockBean
    TextExtractionService teService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void properImageShouldReturnProperQuestions() throws Exception {
        String teServiceExpectedReturn = "Expected return";
        when(teService.extractTextFromImg(any(MultipartFile.class))).thenReturn(teServiceExpectedReturn);

        QuestionDTO expectedResponse = new QuestionDTO("Some question",
                List.of("a1", "a2", "a3"), 1, "Valuable reason");
        when(qgService.generateQuiz(teServiceExpectedReturn)).thenReturn(expectedResponse);

        MockMultipartFile file = new MockMultipartFile(
                "file",                  // Parameter name in the controller
                "test-file.txt",         // Original file name
                MediaType.TEXT_PLAIN_VALUE, // Content type
                "Hello, World!".getBytes() // File content as bytes
        );

        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.multipart("/process").file(file))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        String jsonResult = mvcResult.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        QuestionDTO myResponse = objectMapper.readValue(jsonResult, new TypeReference<>() {});

        assertThat(myResponse).isEqualTo(expectedResponse);
    }
}