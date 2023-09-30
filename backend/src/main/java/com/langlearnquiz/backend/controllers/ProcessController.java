package com.langlearnquiz.backend.controllers;


import com.langlearnquiz.backend.dtos.QuestionDTO;
import com.langlearnquiz.backend.services.QuizGenerationService;
import com.langlearnquiz.backend.services.TextExtractionService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
public class ProcessController {
    @Autowired
    TextExtractionService extService;

    @Autowired
    QuizGenerationService quizGenService;

    /**
     * This method handles the processing of an uploaded file. It extracts text from the file
     * using the external service provided by {@link TextExtractionService}, generates a quiz based on the
     * extracted text using {@link QuizGenerationService}, and returns a list of {@link QuestionDTO} objects
     * representing quiz questions and answers.
     *
     * @param file The uploaded file containing text to be processed.
     * @param response The HTTP servlet response used to set the HTTP status code in case of an error.
     * @return A list of {@link QuestionDTO} objects representing quiz questions and answers, or null
     *         if there is an error during processing.
     */
    @PostMapping("/process")
    public List<QuestionDTO> process(@RequestParam("file") MultipartFile file, HttpServletResponse response){
        Optional<String> textOpt = extService.extractTextFromImg(file);
        if(textOpt.isPresent()) {
            String text = textOpt.get();
            return quizGenService.generateQuiz(text).orElse(null);
        }
        return null;
    }
}
