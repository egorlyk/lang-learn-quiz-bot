package com.langlearnquiz.frontend.handlers.callback;

import com.langlearnquiz.frontend.dtos.UserRequestDTO;
import com.langlearnquiz.frontend.dtos.enums.UserState;
import com.langlearnquiz.frontend.markups.AfterAnswerMenuMarkup;
import com.langlearnquiz.frontend.utils.RestServiceUtil;
import com.langlearnquiz.frontend.utils.TelegramServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

/**
 * Handler responsible for validate user answer to the question. Set user state to QUESTION_ANSWERED_STATE
 */
@Component
public class QuestionResponseCallbackHandler implements CallbackRequestHandler {
    @Autowired
    private TelegramServiceUtil telegramServiceUtil;

    @Autowired
    private RestServiceUtil restServiceUtil;

    @Override
    public boolean isApplicable(UserRequestDTO userRequest) {
        String data = userRequest.getUpdate().getCallbackQuery().getData();
        UserState userState = userRequest.getUser().getUserState();
        return userState == UserState.QUESTION_UNANSWERED_STATE && data.contains("answer");
    }

    @Override
    public void handle(UserRequestDTO userRequest) {
        CallbackQuery callbackQuery = userRequest.getUpdate().getCallbackQuery();
        int chosenAnswerIndex = Integer.parseInt(
                callbackQuery.getData().split("=")[1]);
        long chatId = userRequest.getUser().getChatId();
        int previousMessageId = callbackQuery.getMessage().getMessageId();

        int inlineMessageId = callbackQuery.getMessage().getMessageId();
        telegramServiceUtil.removeMarkup(chatId, inlineMessageId);

        String response = restServiceUtil.getCorrectAnswerResponse(chatId, chosenAnswerIndex);
        InlineKeyboardMarkup markup = AfterAnswerMenuMarkup.getMarkup();
        telegramServiceUtil.sendMessageWithMarkup(chatId, response, markup);

        // Remove previous message for redundancy
        telegramServiceUtil.deleteMessage(chatId, previousMessageId);

        userRequest.getUser().setUserState(UserState.QUESTION_ANSWERED_STATE);
    }
}
