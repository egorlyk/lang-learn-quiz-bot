package com.langlearnquiz.frontend.handlers.message.commands;

import com.langlearnquiz.frontend.dtos.UserRequestDTO;
import com.langlearnquiz.frontend.dtos.enums.UserState;
import com.langlearnquiz.frontend.markups.BackMarkup;
import com.langlearnquiz.frontend.utils.TelegramServiceUtil;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

/**
 * Image command handler. Send to user a request to upload the image to analysis. Accept only uncompressed image.
 * Set user state to IMAGE_REQUEST_STATE
 */
@Component
@ToString(onlyExplicitlyIncluded = true)
public class ImageCommandHandler extends CommandRequestHandlerImpl {
    @ToString.Include
    private final String COMMAND = "/image";

    @Autowired
    TelegramServiceUtil telegramServiceUtil;

    @Override
    public String getCommand() {
        return COMMAND;
    }

    @Override
    public void handleRequest(UserRequestDTO userRequest) {
        long chatId = userRequest.getUser().getChatId();

        String response = """
        Send an uncompressed image to process. Make sure you unchecked "<b>Compress the image</b>"
        """;

        InlineKeyboardMarkup markup = BackMarkup.getMarkup();

        telegramServiceUtil.sendMessageWithMarkup(chatId, response, markup);
        userRequest.getUser().setUserState(UserState.IMAGE_REQUEST_STATE);
    }
}
