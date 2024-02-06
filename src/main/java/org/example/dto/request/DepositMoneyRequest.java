package org.example.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepositMoneyRequest {
    private String email;
    private double amount;
}
