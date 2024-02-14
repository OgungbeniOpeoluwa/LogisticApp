package org.example.service.admin;

import jakarta.validation.constraints.NotNull;
import org.example.data.model.Account;
import org.example.data.model.Administrator;
import org.example.data.model.Delivery;
import org.example.data.model.Transaction;
import org.example.data.repository.AdministratorRepository;
import org.example.dto.request.AccountRequest;
import org.example.dto.request.EmailRequest;
import org.example.dto.response.BookingResponse;
import org.example.exception.UserExistException;
import org.example.service.account.AccountService;
import org.example.service.email.EmailService;
import org.example.service.wallet.WalletService;
import org.example.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AdministratorServiceImpl implements AdministratorService {
    @Autowired
    AdministratorRepository administratorRepository;
    @Autowired
    EmailService emailService;
    @Autowired
    WalletService walletService;
    @Autowired
    AccountService accountService;

    @Override
    public void depositConfirmationEmail(String email, double amount) {
        String description = String.format("""
                Payment request from %s
                Amount %s""",email,amount);
        String title = "Deposit to wallet";
        Administrator admin = administratorRepository.findByUsername("Admin");

        EmailRequest emailRequest = Mapper.emailRequest(admin.getEmail(),title,description);
        emailService.send(emailRequest);

    }

    @Override
    public void updateWallet(String email) {
         walletService.updateWallet(email);
         String title = "Update On Deposit";
         String description ="Hello " +email+ " Your wallet has succesfully be credited,you can now check your balance";
         EmailRequest emailRequest = Mapper.emailRequest(email,title,description);
         emailService.send(emailRequest);


    }


    @Override
    public void UpdateDeliveryEmail(Delivery delivery, String response) {
        if(response.equalsIgnoreCase("Accepted")){
           BigDecimal balance = walletService.deductDeliveryFee(delivery.getCustomerEmail(),delivery.getDeliveryPrice());
           String title = "Update on delivery Request";
           String description = String.format("""
                   Hello %s your Booking request Has Successfully been booked.
                   Your current balance is %s """,delivery.getCustomerEmail(),balance);
           EmailRequest emailRequest = Mapper.emailRequest(delivery.getCustomerEmail(),title,description);
           emailService.send(emailRequest);
        }
        else {
            String title = "Update on delivery Request";
            String description = String.format("""
                    Hello %s your Booking request Was Rejected.
                    We Apologise for any inconveniences """, delivery.getCustomerEmail());
            EmailRequest emailRequest = Mapper.emailRequest(delivery.getCustomerEmail(),title,description);
            emailService.send(emailRequest);
        }

    }

    @Override
    public void cancelBookingEmail(String email,String bookingId,String reason,String customerEmail,double price) {
        walletService.refundBalance(customerEmail,price);
        String title ="Delivery Cancelled";
        String description = String.format("""
                Hello %s
                 Delivery request  with booking id %s has been cancelled.
                 Kindly look below for the reason %s""",email,bookingId,reason);
    EmailRequest emailRequest = Mapper.emailRequest(email,title,description);
    emailService.send(emailRequest);
    }

    @Override
    public void sendTransaction(String email, List<Transaction> transactions, @NotNull String companyName) {
        String title ="Transaction History";
        String description = String.format("""
                Hello %s ,
                Kindly find below your transaction history %s""",companyName,transactions);
        EmailRequest emailRequest = Mapper.emailRequest(email,title,description);
        emailService.send(emailRequest);
    }

    @Override
    public void setUpAccount(AccountRequest accountRequest) {
        Administrator admin = administratorRepository.findByUsername(accountRequest.getUsername());
        if(admin == null)throw new UserExistException("User doesn't exist");
        accountService.setAccount(accountRequest,admin);

    }

    @Override
    public Account getAccount(String admin) {
        Administrator administrator = administratorRepository.findByUsername(admin);
        return accountService.getAccount(administrator);
    }


    @Override
    public void sendBookingEmail(BookingResponse booking, String email) {
        String title ="Request to book your delivery service";
        String description =String.format("""
                look below for more information on delivery request %s
                Kindly login to accept or reject the offer""",booking.getDelivery());
        EmailRequest emailRequest = Mapper.emailRequest(email,title,description);
        emailService.send(emailRequest);
    }
}
