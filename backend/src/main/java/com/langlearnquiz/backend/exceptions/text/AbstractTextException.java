package com.langlearnquiz.backend.exceptions.text;

public abstract class AbstractTextException extends RuntimeException{
    public AbstractTextException(String message){
        super(message);
    }
}
