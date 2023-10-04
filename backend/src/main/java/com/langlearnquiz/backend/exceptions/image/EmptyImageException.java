package com.langlearnquiz.backend.exceptions.image;

public class EmptyImageException extends AbstractFileException{
    public EmptyImageException(String message){
        super(message);
    }
}
