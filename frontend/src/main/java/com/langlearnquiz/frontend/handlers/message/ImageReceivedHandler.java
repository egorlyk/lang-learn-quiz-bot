package com.langlearnquiz.frontend.handlers.message;

import com.langlearnquiz.frontend.dtos.UserDTO;
import com.langlearnquiz.frontend.dtos.UserRequestDTO;
import com.langlearnquiz.frontend.dtos.enums.UserState;
import com.langlearnquiz.frontend.handlers.message.commands.ImageCommandHandler;
import com.langlearnquiz.frontend.markups.AfterChosenTopicMenuMarkup;
import com.langlearnquiz.frontend.utils.RestServiceUtil;
import com.langlearnquiz.frontend.utils.TelegramServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

/**
 * Handler responsible for image to topic transformation. Receive an image for the user after {@link ImageCommandHandler}
 * stage. After topic generated update user object. Also change user state to IMAGE_ANALYZED_STATE
 */
@Component
public class ImageReceivedHandler implements MessageRequestHandler {
    @Autowired
    TelegramServiceUtil telegramServiceUtil;

    @Autowired
    RestServiceUtil restServiceUtil;

    @Override
    public boolean isApplicable(UserRequestDTO userRequest) {
        UserState userState = userRequest.getUser().getUserState();
        Message message = userRequest.getUpdate().getMessage();
        return userState == UserState.IMAGE_REQUEST_STATE && message.hasDocument();
    }

    @Override
    public void handle(UserRequestDTO userRequest) {
        UserDTO user = userRequest.getUser();
        long chatId = user.getChatId();
        String fileId = userRequest.getUpdate().getMessage().getDocument().getFileId();
        String infoText = "<i>Image analysis takes time (about 3-5 seconds)</i>";
        Message message = telegramServiceUtil.sendInfoMessage(chatId, infoText);
        int messageId = message.getMessageId();

        File tgFile = telegramServiceUtil.getFile(fileId);
        String filePath = tgFile.getFilePath();

        ByteArrayResource file = restServiceUtil.getFileFromTelegramServerByFilePath(filePath);

        String topic = restServiceUtil.getTopicFromParsedImage(chatId, file);
        user.setTopic(topic);

        String text = String.format("Generated topic - %s. Now you can perform /question command or " +
                "\"Choose another topic\"", topic);
        InlineKeyboardMarkup markup = AfterChosenTopicMenuMarkup.getMarkup();
        telegramServiceUtil.sendMessageWithMarkup(chatId, text, markup);
        telegramServiceUtil.deleteMessage(chatId, messageId);

        user.setUserState(UserState.IMAGE_ANALYZED_STATE);
    }
}
