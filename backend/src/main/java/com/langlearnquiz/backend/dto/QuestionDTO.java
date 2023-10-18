package com.langlearnquiz.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@RedisHash("person")
public class QuestionDTO implements Serializable {
    @JsonProperty("question")
    private String question;
    @JsonProperty("answers")
    private List<String> answers;
    @JsonProperty("correct_answer_index")
    private int correctAnswerIndex;
    @JsonProperty("reason")
    private String reason;
}
