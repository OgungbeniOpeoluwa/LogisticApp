package org.example.service.customers;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.example.data.model.*;
import org.example.data.repository.CustomerRepository;
import org.example.dto.request.CheckPriceQuotationRequest;
import org.example.dto.request.BookDeliveryRequest;
import org.example.dto.request.*;
import org.example.dto.response.DepositMoneyResponse;
import org.example.dto.response.RegisterResponse;
import org.example.dto.response.BookingResponse;
import org.example.dto.response.LongitudeLatitudeResponse;
import org.example.exception.*;
import org.example.service.email.EmailService;
import org.example.service.admin.AdministratorService;
import org.example.service.delivery.DeliveryService;
import org.example.service.logistic.LogisticsService;
import org.example.util.Mapper;
import org.example.util.DistanceCalculation;
import org.example.util.Verification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    EmailService emailService;
    @Autowired
    AdministratorService administratorService;
    @Autowired
    DeliveryService deliveryService;
    @Autowired
    LogisticsService logisticsService;

    private final Validator validator;
    private final int appPercent  = 20;

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

      RegisterResponse response = new RegisterResponse("Registration completed");
        EmailRequest emailRequest = Mapper.emailRequest(registerRequest.getEmail(),
                "Congratulation "+" you have successfully register",
                "Registration Complete");
        emailService.send(emailRequest);
        return response;
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
    public String bookDelivery(BookDeliveryRequest bookDeliveryRequest) {
        Set<ConstraintViolation<BookDeliveryRequest>> violations = validator.validate(bookDeliveryRequest);
        if(!violations.isEmpty())throw new InputException("invalid input");
        if(!userExist(bookDeliveryRequest.getCustomerEmail()))throw new UserExistException(bookDeliveryRequest.getCustomerEmail() + " doesn't exist");
        if(!isLocked(bookDeliveryRequest.getCustomerEmail()))throw new AppLockedException("Kindly login");
       // String wallet = String.valueOf(walletService.checkBalance(bookDeliveryRequest.getCustomerEmail()));
        CheckPriceQuotationRequest quotationRequest = Mapper.mapQuotation(bookDeliveryRequest);
        double price = getQuote(quotationRequest);
       // if(price > Double.parseDouble(wallet) )throw new InsufficientBalanceException("insufficient wallet balance");
        LogisticCompany logisticCompany = logisticsService.checkLogisticCompany(bookDeliveryRequest.getLogisticCompanyName(),bookDeliveryRequest.getTypeOfVechicle());
        BookingResponse booking = deliveryService.bookDelivery(bookDeliveryRequest,price);
        administratorService.sendBookingEmail(booking,logisticCompany.getEmail());
        return booking.getBookingId();

    }

    @Override
    public double getQuote(CheckPriceQuotationRequest address) {
        Set<ConstraintViolation<CheckPriceQuotationRequest>> violations = validator.validate(address);
        if(!violations.isEmpty())throw new InputException("invalid input");
        String pickupAddress =address.getPickUpStreet()+","+" "+address.getPickUpCity()+","+" "+address.getPickUpState();
        String deliveryAddress = address.getDeliveryStreet()+","+" "+address.getDeliveryCity()+","+" "+address.getDeliveryState();
        LongitudeLatitudeResponse pickUpAddress = DistanceCalculation.location(pickupAddress);
        LongitudeLatitudeResponse deliveryAddresses = DistanceCalculation.location(deliveryAddress);
        double kilometer = DistanceCalculation.distance(pickUpAddress.getLatitude(),pickUpAddress.getLongitude(),+
                deliveryAddresses.getLatitude(),deliveryAddresses.getLongitude());
        String format = String.format("%.1f",calculatePrice(address,kilometer));
        return Double.parseDouble(format);
    }


    @Override
    public List<LogisticCompany> searchForAvailableLogistic() {
       List<LogisticCompany> availableLogisticCompany = logisticsService.findAvailableLogisticCompany();
       if(availableLogisticCompany.isEmpty())throw new NoAvailableException("No Logistic company Available");
        return availableLogisticCompany;
    }

    @Override
    public void cancelBookedDelivery(CustomerCancelBookingRequest cancelBookingRequest) {
        if(!userExist(cancelBookingRequest.getCustomerEmail()))throw new UserExistException(cancelBookingRequest.getCustomerEmail() +" user doesn't exist");
        if(!isLocked(cancelBookingRequest.getCustomerEmail()))throw new InvalidLoginDetail("Kindly login");
        Delivery delivery = deliveryService.cancelDelivery(cancelBookingRequest.getBookingId(),cancelBookingRequest.getCustomerEmail());
        if(delivery.getCompany() !=null) {
            LogisticCompany logisticCompany = logisticsService.resetLogistic(cancelBookingRequest.getCompanyName(), cancelBookingRequest.getBookingId(),delivery.getNameOfVechicle());
            administratorService.cancelBookingEmail(logisticCompany.getEmail(),cancelBookingRequest.getBookingId(),cancelBookingRequest.getReasonOnWhyBookingWasCancelled(),
                    cancelBookingRequest.getCustomerEmail(), delivery.getDeliveryPrice());
        }
       // else walletService.refundBalance(cancelBookingRequest.getCustomerEmail(), delivery.getDeliveryPrice());

    }

    @Override
    public Delivery findDeliveryById(FindABookedDeliveryRequest findABookedDeliveryRequest) {
        if(!userExist(findABookedDeliveryRequest.getCustomerEmail()))throw new UserExistException(findABookedDeliveryRequest.getCustomerEmail() +" user doesn't exist");
        if(!isLocked(findABookedDeliveryRequest.getCustomerEmail()))throw new InvalidLoginDetail("Kindly login");
        Delivery delivery = deliveryService.searchByDeliveryId(findABookedDeliveryRequest.getBookingId(), findABookedDeliveryRequest.getCustomerEmail());
        if(delivery == null)throw new DeliveryException("booking id doesn't exist");
        return delivery;
    }

    @Override
    public String trackOrder(TrackOrderRequest trackOrderRequest) {
        String status = deliveryService.getOrderStatus(trackOrderRequest.getBookingId());
        if(status == null)throw new DeliveryException("Order doesn't exist");
        return status;
    }

    @Override
    public List<Delivery> searchByDeliveryStatus(FindDeliveryByStatus findDeliveryByStatus) {
        if(!userExist(findDeliveryByStatus.getEmail()))throw new UserExistException(findDeliveryByStatus.getEmail() +" user doesn't exist");
        if(!isLocked(findDeliveryByStatus.getEmail()))throw new InvalidLoginDetail("Kindly login");
        List<Delivery> deliveries = deliveryService.searchDeliveryByStatus(findDeliveryByStatus.getDeliveryStatus(),findDeliveryByStatus.getEmail());
        if(deliveries.isEmpty())throw new DeliveryException("No delivery with the status");
        return deliveries;
    }

    @Override
    public List<Delivery> findAllDeliveries(String email) {
        if(!userExist(email))throw new UserExistException(email +" user doesn't exist");
        if(!isLocked(email))throw new InvalidLoginDetail("Kindly login");
        List<Delivery> deliveries = deliveryService.findAllCustomerDelivery(email);
        if(deliveries.isEmpty())throw new NoDeliveryException("No delivery History");
        return deliveries;
    }

    @Override
    public void updateProfile(UpdateProfileRequest updateProfileRequest) {
        Customers customer = customerRepository.findByEmail(updateProfileRequest.getEmail());
        if(!userExist(updateProfileRequest.getEmail()))throw new UserExistException(updateProfileRequest+" user doesn't exist");
        if(!isLocked(updateProfileRequest.getEmail()))throw new InvalidLoginDetail("Kindly login");
        if(updateProfileRequest.getName() != null)customer.setName(updateProfileRequest.getName());
        if(updateProfileRequest.getPhoneNumber() != null){
            if(!Verification.verifyPhoneNumber(updateProfileRequest.getPhoneNumber()))throw new InvalidPhoneNumberException("Invalid number");
            customer.setPhoneNumber(updateProfileRequest.getPhoneNumber());
        }
        if(updateProfileRequest.getStreet() != null && updateProfileRequest.getCity() != null) {
            String address = updateProfileRequest.getStreet() + " "+ updateProfileRequest.getCity()+" "+ updateProfileRequest.getCountry();
            customer.setAddress(address);
        }
        customerRepository.save(customer);


    }

    @Override
    public void setUpProfile(SetUpProfileRequest setUpProfileRequest) {
        Customers customers =customerRepository.findByEmail(setUpProfileRequest.getEmail());
        if(customers == null)throw new UserExistException("User doesn't exist");
        if(!isLocked(setUpProfileRequest.getEmail())) throw new AppLockedException("Kindly login");
        if(!Verification.verifyPhoneNumber(setUpProfileRequest.getPhoneNumber()))throw new InvalidPhoneNumberException("Enter a valid number");
        customers.setName(setUpProfileRequest.getName());
        customers.setPhoneNumber(setUpProfileRequest.getPhoneNumber());
        String address = setUpProfileRequest.getStreet() + " " + setUpProfileRequest.getCity() + " " + setUpProfileRequest.getCountry();
        customers.setAddress(address);
        customerRepository.save(customers);

    }


    private static double calculatePrice(CheckPriceQuotationRequest address, double kilometer) {
        double format = Double.parseDouble(String.format("%.1f",kilometer));
        double price = 0;
        for(VehicleType vehicle: VehicleType.values()){
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
        if( registerRequest.getEmail() == null && registerRequest.getPassword() == null)throw new InvalidPasswordException("Kindly input correct details");
    }
}
