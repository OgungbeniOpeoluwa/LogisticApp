package org.example.controller;

import org.example.dto.request.BookDeliveryRequest;
import org.example.dto.request.CustomersRegisterRequest;
import org.example.dto.request.LoginRequest;
import org.example.dto.response.*;
import org.example.exception.LogisticException;
import org.example.service.customers.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class CustomerController {
    @Autowired
    CustomerService customerService;
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody CustomersRegisterRequest customersRegisterRequest){
        RegisterResponse response = new RegisterResponse();
        try{
            response = customerService.register(customersRegisterRequest);
            return new ResponseEntity<>(new ApiResponse(response,true), HttpStatus.ACCEPTED);
        }
        catch (LogisticException logisticException){
            response.setMessage(logisticException.getMessage());
            return  new ResponseEntity<>(new ApiResponse(response,false),HttpStatus.BAD_REQUEST);
        }

    }
    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody LoginRequest loginRequest){
        LoginResponse loginResponse = new LoginResponse();
        try{
            customerService.login(loginRequest);
            loginResponse.setMessage("Login successful");
            return new ResponseEntity<>(new ApiResponse(loginResponse,true),HttpStatus.OK);
        }
        catch(LogisticException logisticException){
            loginResponse.setMessage(loginResponse.getMessage());
            return new ResponseEntity<>(new ApiResponse(loginResponse,true),HttpStatus.BAD_REQUEST)
        }

    }
    @PostMapping("/order")
    public ResponseEntity<?> bookDelivery(@RequestBody BookDeliveryRequest bookDeliveryRequest){
        BookDeliveryResponse bookingResponse = new BookDeliveryResponse();
        try{
            bookingResponse.setMessage(customerService.bookDelivery(bookDeliveryRequest));
            return new ResponseEntity<>(new ApiResponse(bookingResponse,true),HttpStatus.ACCEPTED);
        }
        catch (LogisticException exception){
            return  new ResponseEntity<>(new ApiResponse(bookingResponse,false),HttpStatus.BAD_REQUEST);
        }
    }

}
