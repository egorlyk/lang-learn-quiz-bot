package com.langlearnquiz.frontend.markups;

import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Arrays;
import java.util.List;


@Getter
enum Buttons {
    INFO_BUTTON("Info", "/info"),
    LOAD_IMAGE_BUTTON("Load Image", "/image"),
    CHOSE_TOPIC_BUTTON("Set topic", "/topic"),
    GET_QUESTION_BUTTON("Get question", "/question");

    private final String text;
    private final String data;

    Buttons(String text, String data) {
        this.text = text;
        this.data = data;
    }

}

/**
 * Main menu markup that has buttons from specified {@link Buttons} enum
 */
public class MainMenuMarkup {
    public static InlineKeyboardMarkup getMarkup() {
        List<InlineKeyboardButton> buttons = Arrays.stream(Buttons.values())
                .map((button) -> InlineKeyboardButton.builder()
                        .text(button.getText())
                        .callbackData(button.getData())
                        .build())
                .toList();


        return InlineKeyboardMarkup.builder()
                .clearKeyboard()
                .keyboard(
                    List.of(
                        buttons.subList(0, 2),
                        buttons.subList(2, buttons.size())
                    )
                )
                .build();
    }
}
