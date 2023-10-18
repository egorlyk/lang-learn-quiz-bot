package com.langlearnquiz.frontend.markups;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

/**
 * Markup that has one inline button. By default, it's "Back to main menu", but it's customizable.
 * Callback data - <b>/back</b>
 */
public class BackMarkup {
    public static InlineKeyboardMarkup getMarkup(String text) {
        InlineKeyboardButton back = InlineKeyboardButton.builder()
                .text(text)
                .callbackData("/back")
                .build();

        return InlineKeyboardMarkup.builder()
                .clearKeyboard()
                .keyboard(List.of(List.of(back)))
                .build();
    }

    public static InlineKeyboardMarkup getMarkup() {
        String text = "<< Back to main menu";

        return getMarkup(text);
    }
}
