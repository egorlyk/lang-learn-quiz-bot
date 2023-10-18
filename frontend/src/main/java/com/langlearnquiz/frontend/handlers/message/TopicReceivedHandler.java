package com.langlearnquiz.frontend.handlers.message;

import com.langlearnquiz.frontend.dtos.UserDTO;
import com.langlearnquiz.frontend.dtos.UserRequestDTO;
import com.langlearnquiz.frontend.dtos.enums.UserState;
import com.langlearnquiz.frontend.handlers.message.commands.TopicCommandHandler;
import com.langlearnquiz.frontend.markups.AfterChosenTopicMenuMarkup;
import com.langlearnquiz.frontend.markups.BackMarkup;
import com.langlearnquiz.frontend.utils.TelegramServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.Objects;

/**
 * Handler responsible for processing user topic after input it in {@link TopicCommandHandler} stage. Set user state
 * to TOPIC_CHOSEN_STATE
 */
@Component
public class TopicReceivedHandler implements MessageRequestHandler{
    @Autowired
    private TelegramServiceUtil telegramServiceUtil;

    @Override
    public boolean isApplicable(UserRequestDTO userRequest) {
        return Objects.equals(userRequest.getUser().getUserState(), UserState.TOPIC_REQUEST_STATE) &&
                !userRequest.getUpdate().getMessage().getText().startsWith("/");
    }

    @Override
    public void handle(UserRequestDTO userRequest) {
        UserDTO user = userRequest.getUser();
        long chatId = user.getChatId();
        Message message = userRequest.getUpdate().getMessage();

        int messageId = message.getMessageId() - 1;
        String topic = message.getText();

        if (topic == null || topic.isEmpty()) {
            String text = "Topic can't be empty.";
            InlineKeyboardMarkup markup = BackMarkup.getMarkup();

            telegramServiceUtil.sendMessageWithMarkup(chatId, text, markup);
            return;
        }

        String text = "Topic is chosen. Now you can perform /question command or \"Choose another topic\"";
        InlineKeyboardMarkup markup = AfterChosenTopicMenuMarkup.getMarkup();
        telegramServiceUtil.sendMessageWithMarkup(chatId, text, markup);
        telegramServiceUtil.deleteMessage(chatId, messageId);

        user.setTopic(topic);
        user.setUserState(UserState.TOPIC_CHOSEN_STATE);
    }
}
