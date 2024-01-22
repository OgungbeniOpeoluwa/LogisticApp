package org.example.exception;

public class InvalidPhoneNumberException extends LogisticException {
    public InvalidPhoneNumberException(String message) {
        super(message);
    }
}
