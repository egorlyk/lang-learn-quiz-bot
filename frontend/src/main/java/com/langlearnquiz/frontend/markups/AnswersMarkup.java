package com.langlearnquiz.frontend.markups;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

/**
 * {@link AnswersMarkup} class responsible for return an {@link InlineKeyboardMarkup} with the answers
 * to the question
 */
public class AnswersMarkup {
    static Logger log = LoggerFactory.getLogger(AnswersMarkup.class);

    /**
     * Returns the markup with answers as the buttons
     *
     * @param answers {@link List<String>} answers list
     * @return {@link InlineKeyboardMarkup} markup with the answer buttons
     */
    public static InlineKeyboardMarkup getMarkup(List<String> answers){
        List<InlineKeyboardButton> buttons =
                answers.stream().
                        map(answer -> InlineKeyboardButton.builder()
                                .text(answer)
                                .callbackData(getDataForCallback(answers, answer))
                                .build())
                        .toList();

        InlineKeyboardButton back = InlineKeyboardButton.builder()
                .text("<< Back to main menu")
                .callbackData("/back")
                .build();

        return InlineKeyboardMarkup.builder()
                .clearKeyboard()
                .keyboard(List.of(buttons, List.of(back)))
                .build();
    }

    private static String getDataForCallback(List<String> answers, String answer){
        log.info("Answer data = " + answers);
        return "answer=" + answers.indexOf(answer);
    }
}
