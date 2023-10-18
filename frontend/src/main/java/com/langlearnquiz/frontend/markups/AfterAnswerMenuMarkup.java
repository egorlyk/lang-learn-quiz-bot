package com.langlearnquiz.frontend.markups;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

/**
 * Markup that must be shown after user answer the question
 */
public class AfterAnswerMenuMarkup{

    public static InlineKeyboardMarkup getMarkup() {
        InlineKeyboardButton next = InlineKeyboardButton.builder()
                .text("Next")
                .callbackData("/next_question")
                .build();

        InlineKeyboardButton back = InlineKeyboardButton.builder()
                .text("<< Back to main menu")
                .callbackData("/back")
                .build();

        return InlineKeyboardMarkup.builder()
                .clearKeyboard()
                .keyboard(List.of(List.of(back, next)))
                .build();
    }
}
