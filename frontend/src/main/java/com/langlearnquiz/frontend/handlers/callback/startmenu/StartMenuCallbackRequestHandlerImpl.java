package com.langlearnquiz.frontend.handlers.callback.startmenu;

import com.langlearnquiz.frontend.dtos.UserRequestDTO;
import com.langlearnquiz.frontend.dtos.enums.UserState;
import com.langlearnquiz.frontend.handlers.callback.CallbackRequestHandlerImpl;

import java.util.Objects;

/**
 * Implementation for {@link StartMenuTopicCallbackRequestHandler} that adds the START_STATE user state as a requirement
 * for handling
 */
public abstract class StartMenuCallbackRequestHandlerImpl extends CallbackRequestHandlerImpl
        implements StartMenuCallbackRequestHandler{
    @Override
    public boolean isApplicable(UserRequestDTO userRequest) {
        UserState userState = userRequest.getUser().getUserState();
        return super.isApplicable(userRequest) && Objects.equals(userState, UserState.START_STATE);
    }
}
