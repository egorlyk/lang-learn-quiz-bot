package com.langlearnquiz.backend.controllers;


import com.langlearnquiz.backend.dao.UserDAO;
import com.langlearnquiz.backend.dto.QuestionDTO;
import com.langlearnquiz.backend.dto.UserDTO;
import com.langlearnquiz.backend.dto.enums.UserState;
import com.langlearnquiz.backend.services.AnswerProcessService;
import com.langlearnquiz.backend.services.UserService;
import com.langlearnquiz.backend.services.QuizGenerationService;
import com.langlearnquiz.backend.services.TextExtractionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ProcessController {
    Logger log = LoggerFactory.getLogger(ProcessController.class);

    @Autowired
    TextExtractionService extService;

    @Autowired
    QuizGenerationService quizGenService;

    @Autowired
    UserService userService;

    /**
     * This method handles the processing of an uploaded file. It extracts text from the file
     * using the external service provided by {@link TextExtractionService}, generates a quiz based on the
     * extracted text using {@link QuizGenerationService}, and returns a list of {@link QuestionDTO} objects
     * representing quiz questions and answers.
     *
     * @param file The uploaded file containing text to be processed.
     * @return A topic that has been generated from image
     */
    @PostMapping("/process")
    public String process(@RequestParam("file") MultipartFile file) {
        String text = extService.extractTextFromImg(file);
        String topic = quizGenService.getTopic(text);
        log.info("Topic: " + topic);

        return topic;
    }

    @GetMapping("/generate")
    public QuestionDTO generateQuestionByTopic(@RequestParam("topic") String topic) {
        return quizGenService.generateQuiz(topic);
    }
}
