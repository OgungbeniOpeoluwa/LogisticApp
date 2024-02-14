package org.example.util;

import org.example.data.model.*;
import org.example.dto.request.BookDeliveryRequest;
import org.example.dto.request.CheckPriceQuotationRequest;
import org.example.dto.request.*;
import org.example.exception.InvalidEmailException;
import org.example.exception.InvalidPasswordException;
import org.example.exception.InvalidPhoneNumberException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Mapper {

    public static Customers MapRegister(CustomersRegisterRequest request){
        if(!Verification.verifyPassword(request.getPassword()))throw new InvalidPasswordException("Weak password");
        if(!Verification.verifyEmail(request.getEmail()))throw new InvalidEmailException("Email not Valid");
        if(!Verification.verifyPhoneNumber(request.getPhoneNumber()))throw new InvalidPhoneNumberException("Enter a valid number");
        Customers customers = new Customers();
        customers.setName(request.getName());
        customers.setEmail(request.getEmail());
        customers.setPhoneNumber(request.getPhoneNumber());
        String encodePassword = encryptPassword(request.getPassword());
        customers.setPassword(encodePassword);
        return customers;

    }

    public static EmailRequest emailRequest(String email,String body,String title){
        EmailRequest emailRequest = new EmailRequest(email,body,title);
        return emailRequest;
    }

    private static String encryptPassword(String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodePassword = bCryptPasswordEncoder.encode(password);
        return encodePassword;
    }


    public static CheckPriceQuotationRequest mapQuotation(BookDeliveryRequest bookDeliveryRequest) {
        CheckPriceQuotationRequest priceQuotationRequest = new CheckPriceQuotationRequest();
        priceQuotationRequest.setDeliveryStreet(bookDeliveryRequest.getReceiverStreet());
        priceQuotationRequest.setDeliveryCity(bookDeliveryRequest.getReceiverCity());
        priceQuotationRequest.setDeliveryState(bookDeliveryRequest.getReceiverState());

        priceQuotationRequest.setPickUpCity(bookDeliveryRequest.getPickUpCity());
        priceQuotationRequest.setPickUpState(bookDeliveryRequest.getPickUpState());
        priceQuotationRequest.setPickUpStreet(bookDeliveryRequest.getPickUpStreet());

        priceQuotationRequest.setCustomerEmail(bookDeliveryRequest.getCustomerEmail());
        priceQuotationRequest.setTypeOfVehicle(bookDeliveryRequest.getTypeOfVechicle());
        priceQuotationRequest.setWeightOfPackage(bookDeliveryRequest.getPackageWeight());
        return priceQuotationRequest;
    }
    public static Delivery mapDelivery(BookDeliveryRequest bookDeliveryRequest){
        Delivery delivery = new Delivery();
        String pickUpAddress = bookDeliveryRequest.getPickUpStreet() +" "+ bookDeliveryRequest.getPickUpCity() + " "+ bookDeliveryRequest.getPickUpState();
        String deliveryAddress = bookDeliveryRequest.getReceiverStreet() +" "+bookDeliveryRequest.getReceiverCity()+ " "+bookDeliveryRequest.getReceiverState();
        delivery.setPickUpAddress(pickUpAddress);
        delivery.setPickUpPhoneNumber(bookDeliveryRequest.getPickUpPhoneNumber());
        delivery.setNameOfVechicle(bookDeliveryRequest.getTypeOfVechicle());
        delivery.setRecieverAddress(deliveryAddress);
        delivery.setRecieverPhoneNumber(bookDeliveryRequest.getReceiverPhoneNumber());
        delivery.setLogisticCompany(bookDeliveryRequest.getLogisticCompanyEmail());
        delivery.setRecieverEmail(bookDeliveryRequest.getReceiverEmail());
        delivery.setRecieverName(bookDeliveryRequest.getReceiverName());
        delivery.setCustomerEmail(bookDeliveryRequest.getCustomerEmail());
        delivery.setTypeOfPackage(bookDeliveryRequest.getTypeOfPackage());
        delivery.setPackageWeight(bookDeliveryRequest.getPackageWeight());
        return delivery;
    }

    public static LogisticCompany mapCompany(LogisticRegisterRequest logisticRegisterRequest){
        LogisticCompany logisticCompany = new LogisticCompany();
        logisticCompany.setCompanyName(logisticRegisterRequest.getCompanyName());
        String password = encryptPassword(logisticRegisterRequest.getPassword());
        logisticCompany.setPassword(password);
        logisticCompany.setAddress(logisticRegisterRequest.getAddress());
        logisticCompany.setEmail(logisticRegisterRequest.getEmail());
        logisticCompany.setCacNumber(logisticRegisterRequest.getCacNumber());
        return logisticCompany;
    }
    public static Vechicle mapVechicle(RegisterVehicleRequest request){
        Vechicle vechicle = new Vechicle();
        vechicle.setVechicleType(request.getVehicleType());
        return vechicle;
    }

    public static Account mapAccount(AccountRequest accountRequest){
        Account account = new Account();
        account.setAccountName(accountRequest.getAccountName());
        account.setBankName(accountRequest.getBankName());
        account.setAccountNumber(accountRequest.getAccountNumber());
        return account;
    }


}
