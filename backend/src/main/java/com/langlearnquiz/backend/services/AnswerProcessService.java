package com.langlearnquiz.backend.services;

import com.langlearnquiz.backend.dto.QuestionDTO;
import com.langlearnquiz.backend.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnswerProcessService {
    @Autowired
    UserService userService;

    public String checkAnswer(long chatId, int correctAnswerIndex) {
        UserDTO user = UserDTO.fromDAO(userService.getUserByChatId(chatId));

        QuestionDTO questionDTO = user.getQuestionDTO();
        String correctAnswer = questionDTO.getAnswers().get(correctAnswerIndex);
        String reason = questionDTO.getReason();

        if(questionDTO.getCorrectAnswerIndex() == correctAnswerIndex) {
            return String.format("%s: Correct answer. \n%s", correctAnswer, reason);
        }
        return String.format("%s: Answer is incorrect. \n%s", correctAnswer, reason);

    }
}
