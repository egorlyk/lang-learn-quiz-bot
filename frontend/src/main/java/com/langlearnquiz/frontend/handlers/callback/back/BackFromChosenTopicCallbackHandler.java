package com.langlearnquiz.frontend.handlers.callback.back;

import com.langlearnquiz.frontend.dtos.UserRequestDTO;
import com.langlearnquiz.frontend.dtos.enums.UserState;
import com.langlearnquiz.frontend.handlers.callback.CallbackRequestHandlerImpl;
import com.langlearnquiz.frontend.handlers.message.commands.TopicCommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Callback handler responsible for handle back button click after topic has been chosen. Transfer the userRequest to
 * {@link TopicCommandHandler}
 */
@Component
public class BackFromChosenTopicCallbackHandler extends CallbackRequestHandlerImpl {
    @Autowired
    TopicCommandHandler topicCommandHandler;

    @Override
    public boolean isApplicable(UserRequestDTO userRequestDTO){
        UserState userState = userRequestDTO.getUser().getUserState();
        return super.isApplicable(userRequestDTO) && (Objects.equals(userState, UserState.TOPIC_CHOSEN_STATE) ||
                Objects.equals(userState, UserState.IMAGE_ANALYZED_STATE));
    }

    @Override
    public void handleRequest(UserRequestDTO userRequest) {
        topicCommandHandler.handleRequest(userRequest);
    }

    @Override
    public String getExpectedData() {
        return "/another-topic";
    }
}
