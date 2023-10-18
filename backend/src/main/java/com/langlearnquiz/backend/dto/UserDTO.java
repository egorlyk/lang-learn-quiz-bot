package com.langlearnquiz.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.langlearnquiz.backend.dao.UserDAO;
import com.langlearnquiz.backend.dto.enums.UserState;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserDTO {
    @JsonProperty("chat_id")
    private long chatId;
    @JsonProperty("topic")
    private String topic;
    @JsonProperty("user_state")
    private UserState userState;
    @JsonProperty("question")
    private QuestionDTO questionDTO;

    public UserDAO convertToDAO(){
        return new UserDAO(chatId, topic, userState.ordinal(), questionDTO);
    }

    public static UserDTO fromDAO(UserDAO userDAO){
        if(userDAO == null){
            return new UserDTO();
        }
        long chatId = userDAO.getId();
        String topic = userDAO.getTopic();
        QuestionDTO questionDTO = userDAO.getQuestionDTO();
        UserState userState = null;
        for (UserState value : UserState.values()) {
            if(value.ordinal() == userDAO.getUserState()) {
                userState = value;
            }
        }

        return new UserDTO(chatId, topic, userState, questionDTO);
    }
}
