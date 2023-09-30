package com.langlearnquiz.backend.exceptions;

public class EmptyImageException extends AbstractFileException{
    public EmptyImageException(String message){
        super(message);
    }
}
