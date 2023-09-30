package com.langlearnquiz.backend.exceptions;

public abstract class AbstractFileException extends RuntimeException{
    public AbstractFileException(String message){
        super(message);
    }
}
