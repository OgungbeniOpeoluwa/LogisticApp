package org.example.controller;

import org.example.dto.request.*;
import org.example.dto.response.*;
import org.example.exception.LogisticException;
import org.example.service.customers.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {
    @Autowired
    CustomerService customerService;
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody CustomersRegisterRequest request){
        try {
           return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ApiResponse<RegisterResponse>(customerService.register(request), true));

        }catch (LogisticException exception){
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<String>(exception.getMessage(),false));
        }

    }
    @PostMapping("/order")
    public ResponseEntity<?> bookDelivery(@RequestBody BookDeliveryRequest deliveryRequest){
        try{
           return ResponseEntity.status(HttpStatus.OK).body(customerService.bookDelivery(deliveryRequest));
        }
        catch (LogisticException exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(exception.getMessage(),false));
        }
    }
    @GetMapping("/trackOrder")
    public ResponseEntity<?> trackOrder(@RequestBody TrackOrderRequest trackOrderRequest){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(customerService.trackOrder(trackOrderRequest));
        }
        catch(LogisticException logisticException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(logisticException,false));
        }
    }
    @GetMapping("/availableCompany")
    public ResponseEntity<?> searchForAvailableCompany(){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(customerService.searchForAvailableLogistic());
        }
        catch(LogisticException logisticException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(logisticException.getMessage(),false));
        }
    }

    @GetMapping("/bookingHistoryByStatus")
    public ResponseEntity<?> getAllBookingByStatus(@RequestBody FindDeliveryByStatus findDeliveryByStatus){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(customerService.searchByDeliveryStatus(findDeliveryByStatus));
        }
        catch (LogisticException logisticException){
           return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(logisticException.getMessage(),false));
        }
    }
    @PostMapping("/cancel")
    public  ResponseEntity<?> cancelDelivery(@RequestBody CustomerCancelBookingRequest cancelBookingRequest){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(customerService.cancelBookedDelivery(cancelBookingRequest));
        }
        catch(LogisticException logisticException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(logisticException.getMessage(),false));
        }
    }
    @GetMapping("/allHistory/{email}")
    public ResponseEntity<?> getAllBookingHistory(@PathVariable(name = "email") String email){
        CustomerBookingHistory customerBookingHistory = new CustomerBookingHistory();
        try{
            customerBookingHistory.setMessage(customerService.findAllDeliveries(email));
            return new ResponseEntity<>(new ApiResponse(customerBookingHistory,true),HttpStatus.FOUND);
        }
        catch (LogisticException logisticException){
            customerBookingHistory.setMessage(logisticException.getMessage());
            return new ResponseEntity<>(new ApiResponse(customerBookingHistory,false),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/deliveryQuote")
    public ResponseEntity<?> getDeliveryPrice(@RequestBody CheckPriceQuotationRequest request){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(customerService.getQuote(request));
        }
        catch (LogisticException logisticException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(logisticException.getMessage(),false));
        }
    }
























}

