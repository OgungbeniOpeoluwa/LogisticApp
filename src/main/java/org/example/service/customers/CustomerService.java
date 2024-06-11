package org.example.service.customers;

import org.example.data.model.Delivery;
import org.example.data.model.LogisticCompany;
import org.example.dto.request.CheckPriceQuotationRequest;
import org.example.dto.request.BookDeliveryRequest;
import org.example.dto.request.*;
import org.example.dto.response.ApiResponse;
import org.example.dto.response.LoginResponse;
import org.example.dto.response.RegisterResponse;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.List;

public interface CustomerService {
    RegisterResponse register(CustomersRegisterRequest registerRequest);
   ApiResponse <String> bookDelivery(BookDeliveryRequest bookDeliveryRequest);

    ApiResponse<Double>getQuote(CheckPriceQuotationRequest address);


    ApiResponse<List<LogisticCompany>>searchForAvailableLogistic();

    ApiResponse<?>cancelBookedDelivery(CustomerCancelBookingRequest cancelBookingRequest);

    Delivery findDeliveryById(FindABookedDeliveryRequest findABookedDeliveryRequest);

    ApiResponse<?> trackOrder(TrackOrderRequest trackOrderRequest);

    ApiResponse<List<Delivery>> searchByDeliveryStatus(FindDeliveryByStatus findDeliveryByStatus);

    List<Delivery> findAllDeliveries(String email);





    void updateProfile(UpdateProfileRequest updateProfileRequest);

    ApiResponse<?> setUpAddress(SetUpAddressRequest setUpProfileRequest);


}
