package com.langlearnquiz.frontend.handlers.callback.startmenu;

import com.langlearnquiz.frontend.dtos.UserRequestDTO;
import com.langlearnquiz.frontend.handlers.message.commands.ImageCommandHandler;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Start menu image button handler. Transfer request to {@link ImageCommandHandler}
 */
@ToString(onlyExplicitlyIncluded = true)
@Component
public class StartMenuImageCallbackRequestHandler extends StartMenuCallbackRequestHandlerImpl{
    @ToString.Include
    private final String MENU_DATA = "/image";

    @Autowired
    ImageCommandHandler imageCommandHandler;

    @Override
    public void handleRequest(UserRequestDTO userRequest) {
        imageCommandHandler.handleRequest(userRequest);
    }

    @Override
    public String getExpectedData() {
        return MENU_DATA;
    }
}
