package com.langlearnquiz.frontend.handlers.callback;

import com.langlearnquiz.frontend.dtos.UserRequestDTO;
import com.langlearnquiz.frontend.dtos.enums.UserState;
import com.langlearnquiz.frontend.handlers.message.commands.QuestionCommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Next question callback handler. Triggered only after QUESTION_ANSWERED_STATE. Generate another question with the
 * same topic. Set user state to QUESTION_UNANSWERED_STATE
 */
@Component
public class NextQuestionCallbackHandler extends CallbackRequestHandlerImpl{
    @Autowired
    private QuestionCommandHandler questionCommandHandler;

    @Override
    public boolean isApplicable(UserRequestDTO userRequestDTO) {
        UserState userState = userRequestDTO.getUser().getUserState();
        return super.isApplicable(userRequestDTO) && Objects.equals(userState,UserState.QUESTION_ANSWERED_STATE);
    }

    @Override
    public String getExpectedData() {
        return "/next_question";
    }

    @Override
    public void handleRequest(UserRequestDTO userRequest) {
        questionCommandHandler.handleRequest(userRequest);
    }
}
