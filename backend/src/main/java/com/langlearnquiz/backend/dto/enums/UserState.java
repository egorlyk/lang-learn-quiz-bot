package com.langlearnquiz.backend.dto.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.NUMBER)
public enum UserState {
    START_STATE,
    IMAGE_REQUEST_STATE,
    TOPIC_REQUEST_STATE,
    QUESTION_ANSWERED_STATE,
    IMAGE_ANALYZED_STATE,
    INFO_SHOWED_STATE,
    TOPIC_CHOSEN_STATE,
    QUESTION_UNANSWERED_STATE
}