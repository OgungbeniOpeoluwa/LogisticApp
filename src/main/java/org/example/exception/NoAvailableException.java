package org.example.exception;

import org.example.data.model.LogisticCompany;

public class NoAvailableException extends LogisticException {
    public NoAvailableException(String message) {
        super(message);
    }
}
