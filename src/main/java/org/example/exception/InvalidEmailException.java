package org.example.exception;

public class InvalidEmailException extends LogisticException{
    public InvalidEmailException(String message) {
        super(message);
    }
}
