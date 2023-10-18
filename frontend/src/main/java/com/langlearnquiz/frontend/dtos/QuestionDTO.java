package com.langlearnquiz.frontend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * Model responsible for transferring question and answers internally and externally
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO implements Serializable {
    /**
     * Generate question
     */
    @JsonProperty("question")
    private String question;
    /**
     * Possible answers to the question
     */
    @JsonProperty("answers")
    private List<String> answers;
    /**
     * Correct answer index
     */
    @JsonProperty("correct_answer_index")
    private int correctAnswerIndex;
    /**
     * Reason, why was that answer chosen
     */
    @JsonProperty("reason")
    private String reason;
}
