package com.langlearnquiz.backend.exceptions.image;

public class EmptyImageFilenameException extends AbstractFileException{
    public EmptyImageFilenameException(String message){
        super(message);
    }
}
