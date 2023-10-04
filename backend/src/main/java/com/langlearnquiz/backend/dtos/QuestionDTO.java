package com.langlearnquiz.backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class QuestionDTO {
    private String question;
    private List<String> answers;
    @JsonProperty("correct_answer_index")
    private int correctAnswerIndex;
    private String reason;
}
