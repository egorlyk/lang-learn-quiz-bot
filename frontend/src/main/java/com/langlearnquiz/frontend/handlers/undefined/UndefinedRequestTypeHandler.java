package com.langlearnquiz.frontend.handlers.undefined;

import com.langlearnquiz.frontend.dtos.UserRequestDTO;
import com.langlearnquiz.frontend.handlers.RequestHandler;
import com.langlearnquiz.frontend.utils.TelegramServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Handler responsible for shown a "Can't handle this type of request". Uses if no one is applicable
 */
@Component
public class UndefinedRequestTypeHandler implements RequestHandler {
    @Autowired
    TelegramServiceUtil telegramServiceUtil;

    @Override
    public boolean isApplicable(UserRequestDTO userRequest) {
        return true;
    }

    @Override
    public void handle(UserRequestDTO userRequest) {
        long chatId = userRequest.getUser().getChatId();

        String text = "I can't handle this type of message. Use buttons or commands (e.g /start)";
        telegramServiceUtil.sendMessageWithoutMarkup(chatId, text);
    }
}
