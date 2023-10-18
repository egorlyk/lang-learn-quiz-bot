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
 * Info command handler. Send user info about the bot. Set user state to INFO_SHOWED_STATE
 */
@ToString(onlyExplicitlyIncluded = true)
@Component
public class InfoCommandHandler extends CommandRequestHandlerImpl {
    @Autowired
    TelegramServiceUtil telegramServiceUtil;

    @ToString.Include
    private final String COMMAND = "/info";

    @Override
    public void handleRequest(UserRequestDTO userRequest) {
        long chatId = userRequest.getUser().getChatId();

        String response = """
                    This is unfinished info about bot. Will be filled from Github README soon
                """;

        InlineKeyboardMarkup markup = BackMarkup.getMarkup();

        telegramServiceUtil.sendMessageWithMarkup(chatId, response, markup);
        userRequest.getUser().setUserState(UserState.INFO_SHOWED_STATE);
    }

    @Override
    public String getCommand() {
        return COMMAND;
    }
}
