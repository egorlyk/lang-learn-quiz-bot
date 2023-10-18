package com.langlearnquiz.frontend.handlers.message.commands;

import com.langlearnquiz.frontend.dtos.QuestionDTO;
import com.langlearnquiz.frontend.dtos.UserDTO;
import com.langlearnquiz.frontend.dtos.UserRequestDTO;
import com.langlearnquiz.frontend.dtos.enums.UserState;
import com.langlearnquiz.frontend.markups.AnswersMarkup;
import com.langlearnquiz.frontend.utils.RestServiceUtil;
import com.langlearnquiz.frontend.utils.TelegramServiceUtil;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

/**
 * Question command handler. Generate a question with previously specified topic. Return error message to user, if
 * topic hasn't been set. Change user state to QUESTION_UNANSWERED_STATE
 */
@ToString(onlyExplicitlyIncluded = true)
@Component
public class QuestionCommandHandler extends CommandRequestHandlerImpl {
    @ToString.Include
    private final String COMMAND = "/question";

    @Autowired
    private RestServiceUtil restServiceUtil;

    @Autowired
    private TelegramServiceUtil telegramServiceUtil;

    @Override
    public void handleRequest(UserRequestDTO userRequest) {
        UserDTO user = userRequest.getUser();
        long chatId = user.getChatId();
        String topic = user.getTopic();

        if(topic == null) {
            String text = "Topic isn't chosen. Download an image to recognize, or " +
                    "chose it with /topic command (or in start menu)";
            telegramServiceUtil.sendInfoMessage(chatId, text);
            return;
        }

        String infoText = "<i>Question generation takes time (about 7-10 seconds)</i>";
        Message message = telegramServiceUtil.sendInfoMessage(chatId, infoText);

        QuestionDTO questionDTO = restServiceUtil.getQuestionWithTopic(topic);

        // Delete info message after result is ready
        telegramServiceUtil.deleteMessage(chatId, message.getMessageId());

        InlineKeyboardMarkup markup = AnswersMarkup.getMarkup(questionDTO.getAnswers());
        telegramServiceUtil.sendMessageWithMarkup(chatId, questionDTO.getQuestion(), markup);

        user.setQuestionDTO(questionDTO);
        user.setUserState(UserState.QUESTION_UNANSWERED_STATE);
    }

    @Override
    public String getCommand() {
        return COMMAND;
    }
}
