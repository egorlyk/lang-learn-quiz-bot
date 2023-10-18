package com.langlearnquiz.frontend.handlers.callback.startmenu;

import com.langlearnquiz.frontend.dtos.UserRequestDTO;
import com.langlearnquiz.frontend.handlers.message.commands.InfoCommandHandler;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Start menu info button handler. Transfer request to {@link InfoCommandHandler}
 */
@ToString(onlyExplicitlyIncluded = true)
@Component
public class StartMenuInfoCallbackRequestHandler extends StartMenuCallbackRequestHandlerImpl{
    @ToString.Include
    private final String MENU_DATA = "/info";

    @Autowired
    InfoCommandHandler infoCommandHandler;

    @Override
    public void handleRequest(UserRequestDTO userRequest) {
        infoCommandHandler.handleRequest(userRequest);
    }

    @Override
    public String getExpectedData() {
        return MENU_DATA;
    }
}
