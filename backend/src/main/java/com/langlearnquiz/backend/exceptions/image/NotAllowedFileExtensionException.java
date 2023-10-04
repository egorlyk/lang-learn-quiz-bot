package com.langlearnquiz.backend.exceptions.image;

public class NotAllowedFileExtensionException extends AbstractFileException {
    public NotAllowedFileExtensionException(String message){
        super(message);
    }
}
