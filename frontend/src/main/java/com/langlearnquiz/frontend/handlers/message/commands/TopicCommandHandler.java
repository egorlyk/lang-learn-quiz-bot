package com.langlearnquiz.frontend.handlers.message.commands;

import com.langlearnquiz.frontend.dtos.UserDTO;
import com.langlearnquiz.frontend.dtos.UserRequestDTO;
import com.langlearnquiz.frontend.dtos.enums.UserState;
import com.langlearnquiz.frontend.markups.BackMarkup;
import com.langlearnquiz.frontend.utils.TelegramServiceUtil;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

/**
 * Topic command handler. Sends a request to specify a topic to the user. Change user state to TOPIC_REQUEST_STATE
 */
@ToString(onlyExplicitlyIncluded = true)
@Component
public class TopicCommandHandler extends CommandRequestHandlerImpl {
    @ToString.Include
    private final String COMMAND = "/topic";

    @Autowired
    TelegramServiceUtil telegramServiceUtil;

    @Override
    public void handleRequest(UserRequestDTO userRequest) {
        UserDTO user = userRequest.getUser();
        long chatId = user.getChatId();
        String currentTopic;
        if (user.getTopic() == null) {
            currentTopic = "Topic is not set yet. You can set one.";
        } else {
            currentTopic = String.format("Current topic : %s. You can choose new one.", user.getTopic());
        }

        String text = currentTopic + "\nTopic must be short (1-5 words) and accurate. " +
                "Good example of topic: present continuous, countable and uncountable nouns, third conditional, etc.";

        InlineKeyboardMarkup markup = BackMarkup.getMarkup();
        telegramServiceUtil.sendMessageWithMarkup(chatId, text, markup);
        user.setUserState(UserState.TOPIC_REQUEST_STATE);
    }

    @Override
    public String getCommand() {
        return COMMAND;
    }
}
