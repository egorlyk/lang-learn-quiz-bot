package com.langlearnquiz.frontend.handlers.message.commands;

import com.langlearnquiz.frontend.dtos.UserRequestDTO;
import com.langlearnquiz.frontend.dtos.enums.UserState;
import com.langlearnquiz.frontend.markups.MainMenuMarkup;
import com.langlearnquiz.frontend.utils.TelegramServiceUtil;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

/**
 * Start command handler. Sends start main menu to user. Change user state to START_STATE
 */
@Component
@ToString(onlyExplicitlyIncluded = true)
public class StartCommandHandler extends CommandRequestHandlerImpl {
    @ToString.Include
    private final String COMMAND = "/start";

    @Autowired
    TelegramServiceUtil telegramServiceUtil;

    @Override
    public void handleRequest(UserRequestDTO userRequest) {
        long chatId = userRequest.getUser().getChatId();

        String response = """
            Hello. Check out the list of commands in the menu below.
        """;

        InlineKeyboardMarkup markup = MainMenuMarkup.getMarkup();
        telegramServiceUtil.sendMessageWithMarkup(chatId, response, markup);
        userRequest.getUser().setUserState(UserState.START_STATE);
    }

    @Override
    public String getCommand() {
        return COMMAND;
    }
}
