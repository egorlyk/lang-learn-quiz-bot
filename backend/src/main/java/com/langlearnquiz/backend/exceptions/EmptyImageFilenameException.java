package com.langlearnquiz.backend.exceptions;

public class EmptyImageFilenameException extends AbstractFileException{
    public EmptyImageFilenameException(String message){
        super(message);
    }
}
