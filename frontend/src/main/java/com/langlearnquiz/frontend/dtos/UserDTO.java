package com.langlearnquiz.frontend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.langlearnquiz.frontend.dtos.enums.UserState;
import lombok.*;

/**
 * Model that responsible for store and transferring current user's state
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class UserDTO{
    /**
     * User-bot unique chatId
     */
    @JsonProperty("chat_id")
    private long chatId;
    /**
     * Current user topics
     */
    @JsonProperty("topic")
    private String topic;
    /**
     * Current user state
     */
    @JsonProperty("user_state")
    private UserState userState;
    /**
     * Current question that attached to the user
     */
    @JsonProperty("question")
    private QuestionDTO questionDTO;
}
