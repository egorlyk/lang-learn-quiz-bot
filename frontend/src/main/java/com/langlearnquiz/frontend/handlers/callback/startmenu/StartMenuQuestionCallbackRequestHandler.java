package com.langlearnquiz.frontend.handlers.callback.startmenu;

import com.langlearnquiz.frontend.dtos.UserRequestDTO;
import com.langlearnquiz.frontend.handlers.message.commands.QuestionCommandHandler;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Start menu question button handler. Transfer request to {@link QuestionCommandHandler}
 */
@ToString(onlyExplicitlyIncluded = true)
@Component
public class StartMenuQuestionCallbackRequestHandler extends StartMenuCallbackRequestHandlerImpl{
    @ToString.Include
    private final String MENU_DATA = "/question";

    @Autowired
    QuestionCommandHandler questionCommandHandler;

    @Override
    public void handleRequest(UserRequestDTO userRequest) {
        questionCommandHandler.handleRequest(userRequest);
    }

    @Override
    public String getExpectedData() {
        return MENU_DATA;
    }
}
