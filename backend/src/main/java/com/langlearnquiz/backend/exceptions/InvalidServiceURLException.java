package com.langlearnquiz.backend.exceptions;

public class InvalidServiceURLException extends RuntimeException{

    public InvalidServiceURLException(String message) {
        super(message);
    }
}
