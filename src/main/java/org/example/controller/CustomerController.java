package org.example.controller;

import com.mysql.cj.log.Log;
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
            loginResponse.setMessage(logisticException.getMessage());
            return new ResponseEntity<>(new ApiResponse(loginResponse,true),HttpStatus.BAD_REQUEST);
        }

    }
    @PostMapping("/order")
    public ResponseEntity<?> bookDelivery(@RequestBody BookDeliveryRequest bookDeliveryRequest){
        BookDeliveryResponse bookingResponse = new BookDeliveryResponse();
        try{
            bookingResponse.setMessage("Your tracking id is " +customerService.bookDelivery(bookDeliveryRequest));
            return new ResponseEntity<>(new ApiResponse(bookingResponse,true),HttpStatus.ACCEPTED);
        }
        catch (LogisticException exception){
            bookingResponse.setMessage(exception.getMessage());
            return  new ResponseEntity<>(new ApiResponse(bookingResponse,false),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/trackOrder")
    public ResponseEntity<?> trackOrder(@RequestBody TrackOrderRequest trackOrderRequest){
        TrackOrderResponse trackOrderResponse = new TrackOrderResponse();
        try{
            trackOrderResponse.setMessage("You order is "+customerService.trackOrder(trackOrderRequest));
            return new ResponseEntity<>(new ApiResponse(trackOrderResponse,true),HttpStatus.OK);
        }
        catch(LogisticException logisticException){
            trackOrderResponse.setMessage(logisticException.getMessage());
            return new ResponseEntity<>(new ApiResponse(trackOrderResponse,false),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/availableCompany")
    public ResponseEntity<?> searchForAvailableCompany(){
        AvailableLogisticCompany availableLogisticCompany = new AvailableLogisticCompany();
        try{
            availableLogisticCompany.setMessage( customerService.searchForAvailableLogistic());
            return new ResponseEntity<>(new ApiResponse(availableLogisticCompany,true),HttpStatus.FOUND);
        }
        catch(LogisticException logisticException){
            availableLogisticCompany.setMessage(logisticException.getMessage());
            return  new ResponseEntity<>(new ApiResponse(availableLogisticCompany,false),HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/bookingHistoryByStatus")
    public ResponseEntity<?> getAllBookingByStatus(@RequestBody FindDeliveryByStatus findDeliveryByStatus){
        FindDeliveryByStatusResponse findDeliveryByStatusResponse = new FindDeliveryByStatusResponse();
        try{
            findDeliveryByStatusResponse.setMessage(customerService.searchByDeliveryStatus(findDeliveryByStatus));
            return new ResponseEntity<>(new ApiResponse(findDeliveryByStatusResponse,true),HttpStatus.FOUND);
        }
        catch (LogisticException logisticException){
            findDeliveryByStatusResponse.setMessage(logisticException.getMessage());
            return new ResponseEntity<>(new ApiResponse(findDeliveryByStatusResponse,false),HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/cancel")
    public  ResponseEntity<?> cancelDelivery(@RequestBody CustomerCancelBookingRequest cancelBookingRequest){
        CancelBookingResponse cancel = new CancelBookingResponse();
        try{
            customerService.cancelBookedDelivery(cancelBookingRequest);
            cancel.setMessage("Your order has been cancelled");
            return new ResponseEntity<>(new ApiResponse(cancel,true),HttpStatus.ACCEPTED);
        }
        catch(LogisticException logisticException){
            cancel.setMessage(logisticException.getMessage());
           return new ResponseEntity<>(new ApiResponse(cancel,false),HttpStatus.BAD_REQUEST);
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






















    //    @PostMapping("/deposit")
//    public ResponseEntity<?> depositToWallet(@RequestBody DepositMoneyRequest depositMoneyRequest){
//        DepositMoneyResponse depositMoneyResponse = new DepositMoneyResponse();
//        try{
//            depositMoneyResponse = customerService.depositToWallet(depositMoneyRequest);
//            return new ResponseEntity<>(new ApiResponse(depositMoneyResponse,true),HttpStatus.ACCEPTED);
//        }
//        catch (LogisticException logisticException){
//            depositMoneyResponse.setMessage(logisticException.getMessage());
//            return new ResponseEntity<>(new ApiResponse(depositMoneyResponse,false),HttpStatus.BAD_REQUEST);
//        }
//    }
//    @GetMapping("/balance/{email}")
//    public ResponseEntity<?> getBalance(@PathVariable(name="email")String email){
//        CheckBalanceResponse checkBalanceResponse = new CheckBalanceResponse();
//        try{
//            checkBalanceResponse.setMessage("your wallet balance is " +customerService.checkBalance(email));
//            return new ResponseEntity<>(new ApiResponse(checkBalanceResponse,true),HttpStatus.FOUND);
//        }
//        catch (LogisticException exception){
//            checkBalanceResponse.setMessage(exception.getMessage());
//            return new ResponseEntity<>(new ApiResponse(checkBalanceResponse,false),HttpStatus.NOT_FOUND);
//        }
//    }


}

