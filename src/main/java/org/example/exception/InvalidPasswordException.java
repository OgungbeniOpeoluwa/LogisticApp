package org.example.exception;

public class InvalidPasswordException extends LogisticException{
    public InvalidPasswordException(String message) {
        super(message);
    }
}
