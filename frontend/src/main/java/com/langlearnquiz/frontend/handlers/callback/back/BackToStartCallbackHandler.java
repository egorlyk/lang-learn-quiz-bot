package com.langlearnquiz.frontend.handlers.callback.back;

import com.langlearnquiz.frontend.dtos.UserRequestDTO;
import com.langlearnquiz.frontend.dtos.enums.UserState;
import com.langlearnquiz.frontend.handlers.message.commands.StartCommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Handler responsible for all "Back to main menu" buttons. Transfer the request to {@link StartCommandHandler}
 */
@Component
public class BackToStartCallbackHandler extends BackCallbackRequestHandlerImpl {
    @Autowired
    StartCommandHandler startCommandHandler;

    @Override
    public List<UserState> getExpectedStates() {
        return List.of(
                UserState.QUESTION_ANSWERED_STATE,
                UserState.QUESTION_UNANSWERED_STATE,
                UserState.TOPIC_REQUEST_STATE,
                UserState.TOPIC_CHOSEN_STATE,
                UserState.IMAGE_REQUEST_STATE,
                UserState.IMAGE_ANALYZED_STATE,
                UserState.INFO_SHOWED_STATE
        );
    }

    @Override
    public void handleRequest(UserRequestDTO userRequest) {
        userRequest.getUser().setUserState(UserState.START_STATE);
        startCommandHandler.handleRequest(userRequest);
    }
}
