package org.example.exception;

import lombok.Data;


public class InsufficientBalanceException extends LogisticException {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
