package org.example.service;

import org.example.data.model.Customers;
import org.example.dto.LoginRequest;
import org.example.dto.RegisterRequest;
import org.example.dto.RegisterResponse;

public interface CustomerService {
    RegisterResponse register(RegisterRequest registerRequest);

    Customers  customer(String email);

    void login(LoginRequest loginRequest);
}
