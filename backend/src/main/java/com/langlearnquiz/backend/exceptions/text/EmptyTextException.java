package com.langlearnquiz.backend.exceptions.text;

public class EmptyTextException extends RuntimeException {
    public EmptyTextException(String text) {
        super(text);
    }
}
