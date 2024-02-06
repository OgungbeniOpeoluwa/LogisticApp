package org.example.service.customers;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.example.data.model.Customers;
import org.example.data.model.TypeOfVehicle;
import org.example.data.model.Wallet;
import org.example.data.repository.CustomerRepository;
import org.example.dto.CheckPriceQuotationRequest;
import org.example.dto.BookDeliveryRequest;
import org.example.dto.request.DepositMoneyRequest;
import org.example.dto.request.EmailRequest;
import org.example.dto.request.LoginRequest;
import org.example.dto.request.CustomersRegisterRequest;
import org.example.dto.RegisterResponse;
import org.example.dto.response.LongitudeLatitudeResponse;
import org.example.exception.*;
import org.example.service.email.EmailService;
import org.example.service.admin.AdministratorService;
import org.example.service.delivery.DeliveryService;
import org.example.service.logistic.LogisticsService;
import org.example.service.wallet.WalletService;
import org.example.util.Mapper;
import org.example.util.VerifyRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    EmailService emailService;
    @Autowired
    WalletService walletService;
    @Autowired
    AdministratorService administratorService;
    @Autowired
    DeliveryService deliveryService;
    @Autowired
    LogisticsService logisticsService;

    private final Validator validator;

    public CustomerServiceImpl(Validator validator) {
        this.validator = validator;
    }


    @Override
    public RegisterResponse register(CustomersRegisterRequest registerRequest) {
        Set<ConstraintViolation<CustomersRegisterRequest>> violations = validator.validate(registerRequest);
        if(!violations.isEmpty())throw new InputException("Invalid input");
        checkIfRegisterRequestIsNull(registerRequest);
        if(userExist(registerRequest.getEmail()))throw new UserExistException("User Already Exist");

        Customers customer = Mapper.MapRegister(registerRequest);
        customerRepository.save(customer);
        Wallet wallet = walletService.createWallet(customer,registerRequest.getEmail());
        customer.setWallet(wallet);
        customerRepository.save(customer);


        RegisterResponse response = new RegisterResponse("Registration completed");
        EmailRequest emailRequest = Mapper.emailRequest(registerRequest.getEmail(),
                "Congratulation "+registerRequest.getName()+" you have successfully register",
                "Registration Complete");
        emailService.send(emailRequest);
        return response;
    }

    @Override
    public Customers customer(String email) {
        return null;
    }

    private boolean userExist(String email){
        Customers customers = customerRepository.findByEmail(email);
        return customers != null;
    }



    @Override
    public void login(LoginRequest loginRequest) {
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);
        if(!violations.isEmpty())throw new InputException("Invalid input");
        Customers customers = customerRepository.findByEmail(loginRequest.getEmail());
        if(customers== null)throw new InvalidLoginDetail("invalid details");
        if(customers.isLoginStatus())throw new ActionDoneException("User has already login");
        if(!checkPassword(customers.getEmail(),loginRequest.getPassword())) throw new InvalidLoginDetail("invalid login details");
        customers.setLoginStatus(true);
        customerRepository.save(customers);




    }

    @Override
    public void bookDelivery(BookDeliveryRequest bookDeliveryRequest) {
        Set<ConstraintViolation<BookDeliveryRequest>> violations = validator.validate(bookDeliveryRequest);
        if(!violations.isEmpty())throw new InputException("invalid input");
        userExist(bookDeliveryRequest.getCustomerEmail());
        if(!isLocked(bookDeliveryRequest.getCustomerEmail()))throw new AppLockedException("Kindly login");
        String wallet = String.valueOf(walletService.checkBalance(bookDeliveryRequest.getCustomerEmail()));
        CheckPriceQuotationRequest quotationRequest = Mapper.mapQuotation(bookDeliveryRequest);
        double price = getQuote(quotationRequest);
        if(price > Double.parseDouble(wallet) )throw new InsufficientBalanceException("insufficient wallet balance");
            String bookingId = deliveryService.bookDelivery(bookDeliveryRequest);

    }

    @Override
    public double getQuote(CheckPriceQuotationRequest address) {
        Set<ConstraintViolation<CheckPriceQuotationRequest>> violations = validator.validate(address);
        if(!violations.isEmpty())throw new InputException("invalid input");
        userExist(address.getCustomerEmail());
        if(!isLocked(address.getCustomerEmail()))throw new AppLockedException("Kindly login");
        String pickupAddress =address.getPickUpStreet()+","+" "+address.getPickUpCity()+","+" "+address.getPickUpState();
        String deliveryAddress = address.getDeliveryStreet()+","+" "+address.getDeliveryCity()+","+" "+address.getDeliveryState();
        LongitudeLatitudeResponse pickUpAddress = VerifyRequest.location(pickupAddress);
        LongitudeLatitudeResponse deliveryAddresses = VerifyRequest.location(deliveryAddress);
        double kilometer = VerifyRequest.distance(pickUpAddress.getLatitude(),pickUpAddress.getLongitude(),+
                deliveryAddresses.getLatitude(),deliveryAddresses.getLongitude());
        String format = String.format("%.1f",calculatePrice(address,kilometer));
        return Double.parseDouble(format);
    }

    @Override
    public void depositToWallet(DepositMoneyRequest depositMoneyRequest) {
        userExist(depositMoneyRequest.getEmail());
        if(!isLocked(depositMoneyRequest.getEmail()))throw new AppLockedException("Kindly login");
        Customers customers = customerRepository.findByEmail(depositMoneyRequest.getEmail());
        Wallet wallet = walletService.depositMoney(depositMoneyRequest.getAmount(),depositMoneyRequest.getEmail());
        customers.setWallet(wallet);
        administratorService.depositConfirmationEmail(depositMoneyRequest.getEmail(), depositMoneyRequest.getAmount());
        customerRepository.save(customers);

    }

    @Override
    public BigDecimal checkBalance(String email) {
        userExist(email);
        if(!isLocked(email))throw new AppLockedException("Kindly login");
        return walletService.checkBalance(email);
    }

    private static double calculatePrice(CheckPriceQuotationRequest address, double kilometer) {
        double format = Double.parseDouble(String.format("%.1f",kilometer));
        double price = 0;
        for(TypeOfVehicle vehicle: TypeOfVehicle.values()){
            if(vehicle.name().equalsIgnoreCase(address.getTypeOfVehicle()) && kilometer > 0){
                price = (format * vehicle.ratePerKm) + (address.getWeightOfPackage() * vehicle.ratePerWeight);
            } else if (vehicle.name().equalsIgnoreCase(address.getTypeOfVehicle()) && kilometer == 0){
                price= (1000 * vehicle.ratePerKm) + (address.getWeightOfPackage() * vehicle.ratePerWeight);

            }
        }
        return price;
    }

    private boolean isLocked(String customerEmail) {
        Customers customers = customerRepository.findByEmail(customerEmail);
        return customers.isLoginStatus();
    }

    private boolean checkPassword(String email,String password){
       Customers customers = customerRepository.findByEmail(email);
        BCryptPasswordEncoder decodePassword = new BCryptPasswordEncoder();
      return decodePassword.matches(password,customers.getPassword());
    }

    private static void checkIfRegisterRequestIsNull(CustomersRegisterRequest registerRequest) {
        if(registerRequest.getName() == null && registerRequest.getEmail() == null &&
                registerRequest.getPassword() == null && registerRequest.getAddress() == null &&
                registerRequest.getPhoneNumber() == null)throw new InvalidPasswordException("Kindly input correct details");
    }
}
