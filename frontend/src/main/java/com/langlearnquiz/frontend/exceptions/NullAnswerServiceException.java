package com.langlearnquiz.frontend.exceptions;

/**
 * Exception that can be thrown if answer from service via RestTemplate would be null
 */
public class NullAnswerServiceException extends RuntimeException{
    public NullAnswerServiceException(String message) {
        super(message);
    }
}
