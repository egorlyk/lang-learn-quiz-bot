package com.langlearnquiz.backend.controllers;

import com.langlearnquiz.backend.services.AnswerProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckAnswerController {
    @Autowired
    AnswerProcessService answerProcessService;

    Logger log = LoggerFactory.getLogger(CheckAnswerController.class);

    @GetMapping("/check")
    public String checkAnswer(@RequestParam("chatId") long chatId, @RequestParam("answerIndex") int answerIndex){
        log.info(String.format("params: chatId=%s, answerIndex=%s", chatId, answerIndex));

        return answerProcessService.checkAnswer(chatId, answerIndex);
    }
}
