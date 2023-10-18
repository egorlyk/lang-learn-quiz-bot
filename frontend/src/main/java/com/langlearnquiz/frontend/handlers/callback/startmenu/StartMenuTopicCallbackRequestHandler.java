package com.langlearnquiz.frontend.handlers.callback.startmenu;

import com.langlearnquiz.frontend.dtos.UserRequestDTO;
import com.langlearnquiz.frontend.handlers.message.commands.TopicCommandHandler;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Start menu topic button handler. Transfer request to {@link TopicCommandHandler}
 */
@ToString(onlyExplicitlyIncluded = true)
@Component
public class StartMenuTopicCallbackRequestHandler extends StartMenuCallbackRequestHandlerImpl{
    @ToString.Include
    private final String MENU_DATA = "/topic";

    @Autowired
    TopicCommandHandler topicCommandHandler;

    @Override
    public void handleRequest(UserRequestDTO userRequest) {
        topicCommandHandler.handleRequest(userRequest);
    }

    @Override
    public String getExpectedData() {
        return MENU_DATA;
    }
}
