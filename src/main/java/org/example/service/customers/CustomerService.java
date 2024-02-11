package org.example.service.customers;

import org.example.data.model.Customers;
import org.example.data.model.Delivery;
import org.example.data.model.LogisticCompany;
import org.example.dto.CheckPriceQuotationRequest;
import org.example.dto.BookDeliveryRequest;
import org.example.dto.request.*;
import org.example.dto.RegisterResponse;

import java.math.BigDecimal;
import java.util.List;

public interface CustomerService {
    RegisterResponse register(CustomersRegisterRequest registerRequest);

    void login(LoginRequest loginRequest);

    String bookDelivery(BookDeliveryRequest bookDeliveryRequest);

    double getQuote(CheckPriceQuotationRequest address);

    void depositToWallet(DepositMoneyRequest depositMoneyRequest);

    BigDecimal checkBalance(String email);

    List<LogisticCompany> searchForAvailableLogistic(String email);

    void cancelBookedDelivery(CustomerCancelBookingRequest cancelBookingRequest);

    Delivery findDeliveryById(FindABookedDeliveryRequest findABookedDeliveryRequest);

    String trackOrder(TrackOrderRequest trackOrderRequest);

    List<Delivery> searchByDeliveryStatus(FindDeliveryByStatus findDeliveryByStatus);

    List<Delivery> findAllDeliveries(String email);


}
