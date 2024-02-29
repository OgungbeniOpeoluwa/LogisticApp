package org.example.controller;

import org.example.dto.request.*;
import org.example.dto.response.*;
import org.example.exception.LogisticException;
import org.example.service.logistic.LogisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/logistic")
public class LogisticController {
    @Autowired
    LogisticsService logisticsService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody LogisticRegisterRequest logisticRegisterRequest){
        LogisticRegisterResponse response = new LogisticRegisterResponse();
        try{
            logisticsService.register(logisticRegisterRequest);
            response.setMessage("Registration completed");
            return new ResponseEntity<>(new ApiResponse(response,true), HttpStatus.CREATED);
        }
        catch(LogisticException logisticException){
            response.setMessage(logisticException.getMessage());
            return new ResponseEntity<>(new ApiResponse(response,false),HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody CompanyLoginRequest loginRequest){
        LogisticLoginResponse loginResponse = new LogisticLoginResponse();
        try{
            logisticsService.login(loginRequest);
            loginResponse.setMessage("You have Successfully login");
            return new ResponseEntity<>(new ApiResponse(loginResponse,true),HttpStatus.OK);
        }
        catch(LogisticException logisticException){
            loginResponse.setMessage(logisticException.getMessage());
            return new ResponseEntity<>(new ApiResponse(loginResponse,false),HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/acceptOrder")
    public ResponseEntity<?> acceptBookingOrder(@RequestBody AcceptBookingRequest bookingRequest){
        AcceptBookingResponse acceptBookingResponse = new AcceptBookingResponse();
        try{
            logisticsService.responseToBookingRequest(bookingRequest);
             acceptBookingResponse.setMessage("Booking being process");
             return new ResponseEntity<>(new ApiResponse(acceptBookingResponse,true),HttpStatus.OK);
        }catch (LogisticException logisticException){
            acceptBookingResponse.setMessage(logisticException.getMessage());
            return new ResponseEntity<>(new ApiResponse(acceptBookingResponse,false),HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/update")
    public ResponseEntity<?> updateDelivery(@RequestBody UpdateDeliveryStatusRequest updateDeliveryStatusRequest){
        UpdateDeliveryResponse updateDeliveryResponse = new UpdateDeliveryResponse();
        try {
            logisticsService.updateDeliveryStatus(updateDeliveryStatusRequest);
            updateDeliveryResponse.setMessage("Delivery Has Been updated");
            return  new ResponseEntity<>(new ApiResponse(updateDeliveryResponse,true),HttpStatus.ACCEPTED);
        }
        catch (LogisticException logisticException){
            updateDeliveryResponse.setMessage(logisticException.getMessage());
            return  new ResponseEntity<>(new ApiResponse(updateDeliveryResponse,false),HttpStatus.BAD_REQUEST);

        }
    }

    @PostMapping("/vechicle")
    public  ResponseEntity<?> registerVechicle(@RequestBody RegisterVehicleRequest registerVehicleRequest){
        RegisterVechicleResponse response = new RegisterVechicleResponse();
        try{
            logisticsService.registerVechicle(registerVehicleRequest);
            response.setMessage("You have successfully register your vechicle");
            return new ResponseEntity<>(new ApiResponse(response,true),HttpStatus.ACCEPTED);
        }
        catch (LogisticException logisticException){
            response.setMessage(logisticException.getMessage());
            return  new ResponseEntity<>(new ApiResponse(response,false),HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/limit")
    public ResponseEntity<?> setVechicleDayLimit(@RequestBody SetDayAvailabiltyRequest availabiltyRequest){
        SetDayAvailabiltyResponse availabiltyResponse = new SetDayAvailabiltyResponse();
        try{
            logisticsService.setDayAvailability(availabiltyRequest);
            availabiltyResponse.setMessage("Vechilce Day Limit as been set Successfully");
            return new ResponseEntity<>( new ApiResponse(availabiltyResponse,true),HttpStatus.OK);
        }
        catch (LogisticException logisticException){
           availabiltyResponse.setMessage(logisticException.getMessage());
           return new ResponseEntity<>(new ApiResponse(availabiltyResponse,false),HttpStatus.BAD_REQUEST);
        }
    }
}
