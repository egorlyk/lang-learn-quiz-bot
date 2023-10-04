package com.langlearnquiz.backend.exceptions.image;

public abstract class AbstractFileException extends RuntimeException{
    public AbstractFileException(String message){
        super(message);
    }
}
