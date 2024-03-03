package org.example.service.admin;

import org.example.data.model.Account;
import org.example.data.model.Delivery;
import org.example.data.model.Transaction;
import org.example.dto.request.AccountRequest;
import org.example.dto.response.BookingResponse;

import java.util.List;

public interface AdministratorService {
    void depositConfirmationEmail(String email, double amount);


    void sendBookingEmail(BookingResponse bookingResponse,String email);

    void UpdateDeliveryEmail(Delivery delivery, String response);

    void cancelBookingEmail(String email, String bookId,String reason,String customerEmail,double price);


    void sendTransaction(String email, List<Transaction> transactions,String companyName);

    void setUpAccount(AccountRequest accountRequest);
    Account getAccount(String admin);
}
