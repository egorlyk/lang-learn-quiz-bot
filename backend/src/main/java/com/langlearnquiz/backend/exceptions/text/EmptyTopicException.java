package com.langlearnquiz.backend.exceptions.text;

import com.langlearnquiz.backend.exceptions.text.AbstractTextException;

public class EmptyTopicException extends AbstractTextException {
    public EmptyTopicException(String message) {
        super(message);
    }
}
