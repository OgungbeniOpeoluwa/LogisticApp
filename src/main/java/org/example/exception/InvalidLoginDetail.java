package org.example.exception;

public class InvalidLoginDetail extends LogisticException{
    public InvalidLoginDetail(String message) {
        super(message);
    }
}
