package org.example.exception;

public class UserExistException extends LogisticException {
    public UserExistException(String message) {
        super(message);
    }
}
