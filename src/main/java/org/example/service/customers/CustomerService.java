package org.example.service.customers;

import org.example.data.model.Customers;
import org.example.dto.CheckPriceQuotationRequest;
import org.example.dto.BookDeliveryRequest;
import org.example.dto.request.DepositMoneyRequest;
import org.example.dto.request.LoginRequest;
import org.example.dto.request.CustomersRegisterRequest;
import org.example.dto.RegisterResponse;

import java.math.BigDecimal;

public interface CustomerService {
    RegisterResponse register(CustomersRegisterRequest registerRequest);

    Customers  customer(String email);

    void login(LoginRequest loginRequest);

    void bookDelivery(BookDeliveryRequest bookDeliveryRequest);

    double getQuote(CheckPriceQuotationRequest address);

    void depositToWallet(DepositMoneyRequest depositMoneyRequest);

    BigDecimal checkBalance(String email);
}
