package org.example.util;

import org.example.data.model.*;
import org.example.dto.request.BookDeliveryRequest;
import org.example.dto.request.CheckPriceQuotationRequest;
import org.example.dto.request.*;
import org.example.data.CompanieDelivery;
import org.example.exception.InvalidEmailException;
import org.example.exception.InvalidPasswordException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Mapper {

    public static Customers MapRegister(CustomersRegisterRequest request){
        if(!Verification.verifyPassword(request.getPassword()))throw new InvalidPasswordException("Weak password");
        if(!Verification.verifyEmail(request.getEmail()))throw new InvalidEmailException("Email not Valid");
        Customers customers = new Customers();
        customers.setEmail(request.getEmail());
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
        priceQuotationRequest.setDeliveryCity(bookDeliveryRequest.getReceiverArea());
        priceQuotationRequest.setDeliveryState(bookDeliveryRequest.getReceiverState());

        priceQuotationRequest.setPickUpCity(bookDeliveryRequest.getPickUpArea());
        priceQuotationRequest.setPickUpState(bookDeliveryRequest.getPickUpState());
        priceQuotationRequest.setPickUpStreet(bookDeliveryRequest.getPickUpStreet());

        priceQuotationRequest.setTypeOfVehicle(bookDeliveryRequest.getTypeOfVechicle());
        priceQuotationRequest.setWeightOfPackage(bookDeliveryRequest.getPackageWeight());
        return priceQuotationRequest;
    }
    public static Delivery mapDelivery(BookDeliveryRequest bookDeliveryRequest){
        Delivery delivery = new Delivery();
        String pickUpAddress = bookDeliveryRequest.getPickUpStreet() +" "+ bookDeliveryRequest.getPickUpArea() + " "+ bookDeliveryRequest.getPickUpState();
        String deliveryAddress = bookDeliveryRequest.getReceiverStreet() +" "+bookDeliveryRequest.getReceiverArea()+ " "+bookDeliveryRequest.getReceiverState();
        delivery.setPickUpAddress(pickUpAddress);
        delivery.setPickUpPhoneNumber(bookDeliveryRequest.getPickUpPhoneNumber());
        delivery.setNameOfVechicle(bookDeliveryRequest.getTypeOfVechicle());
        delivery.setRecieverAddress(deliveryAddress);
        delivery.setRecieverPhoneNumber(bookDeliveryRequest.getReceiverPhoneNumber());
        delivery.setLogisticCompany(bookDeliveryRequest.getLogisticCompanyName());
        delivery.setRecieverEmail(bookDeliveryRequest.getReceiverEmail());
        delivery.setRecieverName(bookDeliveryRequest.getReceiverName());
        delivery.setCustomerEmail(bookDeliveryRequest.getCustomerEmail());
        delivery.setTypeOfPackage(bookDeliveryRequest.getTypeOfPackage());
        delivery.setPackageWeight(bookDeliveryRequest.getPackageWeight());
        return delivery;
    }
    public static CompanieDelivery mapDelivery(Delivery delivery){
        CompanieDelivery deliveries = new CompanieDelivery();
        deliveries.setDeliveryStatus(delivery.getDeliveryStatus());
        deliveries.setDeliveryPrice(delivery.getDeliveryPrice());
        deliveries.setCompanyAmount(deliveries.getCompanyAmount());
        deliveries.setRecieverName(delivery.getRecieverName());
        deliveries.setRecieverPhoneNumber(deliveries.getRecieverPhoneNumber());
        deliveries.setRecieverAddress(deliveries.getRecieverAddress());
        deliveries.setBookingId(delivery.getBookingId());
        deliveries.setPickUpAddress(delivery.getPickUpAddress());
        deliveries.setPickUpPhoneNumber(delivery.getPickUpPhoneNumber());
        deliveries.setPickUpUserName(deliveries.getPickUpUserName());
        deliveries.setPackageWeight(delivery.getPackageWeight());
        deliveries.setNameOfVechicle(delivery.getNameOfVechicle());
        deliveries.setCustomerEmail(deliveries.getCustomerEmail());
        deliveries.setTypeOfPackage(delivery.getTypeOfPackage());
        deliveries.setLogisticCompany(delivery.getLogisticCompany());
        return deliveries;

    }

    public static LogisticCompany mapCompany(LogisticRegisterRequest logisticRegisterRequest){
        LogisticCompany logisticCompany = new LogisticCompany();
        logisticCompany.setCompanyName(logisticRegisterRequest.getCompanyName());
        String password = encryptPassword(logisticRegisterRequest.getPassword());
        logisticCompany.setPassword(password);
        logisticCompany.setPhoneNumber(logisticCompany.getPhoneNumber());
        logisticCompany.setAddress(logisticRegisterRequest.getAddress());
        logisticCompany.setEmail(logisticRegisterRequest.getEmail());
        logisticCompany.setCacNumber(logisticRegisterRequest.getCacNumber());
        return logisticCompany;
    }
    public static Vechicle mapVechicle(RegisterVehicleRequest request, VehicleType status){
        Vechicle vechicle = new Vechicle();
        vechicle.setVechicleType(status);
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
