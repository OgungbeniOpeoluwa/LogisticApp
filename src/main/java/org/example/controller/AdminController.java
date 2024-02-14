package org.example.controller;

import org.example.dto.request.AccountRequest;
import org.example.dto.response.AccountResponse;
import org.example.dto.response.ApiResponse;
import org.example.exception.LogisticException;
import org.example.service.admin.AdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    @Autowired
    AdministratorService administratorService;
    @PostMapping("/account")
    public ResponseEntity<?> setAccount(@RequestBody AccountRequest accountRequest){
        AccountResponse accountResponse = new AccountResponse();
        try{
            administratorService.setUpAccount(accountRequest);
            accountResponse.setMessage("You have successfully set up your Bank Account");
            return new ResponseEntity<>(new ApiResponse(accountResponse,true), HttpStatus.OK);
        }
        catch (LogisticException logisticException){
            accountResponse.setMessage(logisticException.getMessage());
            return new ResponseEntity<>(new ApiResponse(accountResponse,false),HttpStatus.BAD_REQUEST);
        }
    }
}
