package com.langlearnquiz.backend.exceptions;

public class NotAllowedFileExtensionException extends AbstractFileException {
    public NotAllowedFileExtensionException(String message){
        super(message);
    }
}
