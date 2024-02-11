package org.example.exception;

import org.example.data.model.LogisticCompany;

public class NoDeliveryException extends LogisticException {
    public NoDeliveryException(String message) {
        super(message);
    }
}
