package org.example.service.customers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.JsonPatchOperation;
import com.github.fge.jsonpatch.ReplaceOperation;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.example.beanConfig.ConfigBean;
import org.example.data.model.*;
import org.example.data.repository.CustomerRepository;
import org.example.dto.WalletRegisterRequest;
import org.example.dto.request.CheckPriceQuotationRequest;
import org.example.dto.request.BookDeliveryRequest;
import org.example.dto.request.*;
import org.example.dto.response.*;
import org.example.exception.*;
import org.example.security.UserConfig;
import org.example.service.AddressService;
import org.example.service.email.EmailService;
import org.example.service.admin.AdministratorService;
import org.example.service.delivery.DeliveryService;
import org.example.service.logistic.LogisticsService;
import org.example.service.wallet.WalletService;
import org.example.util.Mapper;
import org.example.util.DistanceCalculation;
import org.example.util.Verification;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.*;

import static org.example.data.model.Roles.CUSTOMER;
import static org.example.util.GlobalMessage.*;

@Service
@Slf4j
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService, UserDetailsService {
   private CustomerRepository customerRepository;
    private  EmailService emailService;
    private  AdministratorService administratorService;
   private DeliveryService deliveryService;
   private LogisticsService logisticsService;
   private AddressService addressService;
   private ModelMapper modelMapper;
   private WalletService walletService;


     private final Validator validator;
     private final int appPercent  = 20;



    @Override
    public RegisterResponse register(CustomersRegisterRequest registerRequest) {
        Set<ConstraintViolation<CustomersRegisterRequest>> violations = validator.validate(registerRequest);
        if(!violations.isEmpty())throw new InputException("Invalid input");

        checkIfRegisterRequestIsNull(registerRequest);

        if(userExist(registerRequest.getEmail()).isPresent())throw new UserExistException("User Already Exist");

        Customers customer = Mapper.MapRegister(registerRequest);
        customerRepository.save(customer);

        WalletRegisterRequest wallet = modelMapper.map(registerRequest,WalletRegisterRequest.class);

        WalletRegisterResponse walletResponse = walletService.createWallet(wallet);

       RegisterResponse response = new RegisterResponse("Registration completed");
        EmailRequest emailRequest = Mapper.emailRequest(registerRequest.getEmail(),
                "Congratulation "+" you have successfully register",
                "Registration Complete");
        emailService.send(emailRequest);
        return response;
    }


    private Optional<Customers> userExist(String email){
        return customerRepository.findByEmail(email);
    }


    @Override
    public ApiResponse<String> bookDelivery(BookDeliveryRequest bookDeliveryRequest) {
        Set<ConstraintViolation<BookDeliveryRequest>> violations = validator.validate(bookDeliveryRequest);

        if(!violations.isEmpty())throw new InputException("invalid input");

        Customers customers = userExist(bookDeliveryRequest.getCustomerEmail())
                .orElseThrow(()->new UserExistException("User doesn't exist"));
        String wallet = String.valueOf(walletService.checkBalance(customers.getWalletAuthorization()));

        CheckPriceQuotationRequest quotationRequest = Mapper.mapQuotation(bookDeliveryRequest);

        double price = getQuote(quotationRequest).getMessage();

        if(price > Double.parseDouble(wallet) )throw new InsufficientBalanceException("insufficient wallet balance");

        LogisticCompany logisticCompany = logisticsService.checkLogisticCompany(bookDeliveryRequest.getLogisticCompanyName(),bookDeliveryRequest.getTypeOfVechicle());

        BookingResponse booking = deliveryService.bookDelivery(bookDeliveryRequest,price);

        administratorService.sendBookingEmail(booking,logisticCompany.getEmail());
        return new ApiResponse<>(booking.getBookingId(),true);

    }

    @Override
    public ApiResponse<Double> getQuote(CheckPriceQuotationRequest address) {
        Set<ConstraintViolation<CheckPriceQuotationRequest>> violations = validator.validate(address);
        if(!violations.isEmpty())throw new InputException("invalid input");

        String pickupAddress =address.getPickUpStreet()+","+" "+address.getPickUpCity()+","+" "+address.getPickUpState();

        String deliveryAddress = address.getDeliveryStreet()+","+" "+address.getDeliveryCity()+","+" "+address.getDeliveryState();
        LongitudeLatitudeResponse pickUpAddress = DistanceCalculation.location(pickupAddress);
        LongitudeLatitudeResponse deliveryAddresses = DistanceCalculation.location(deliveryAddress);

        double kilometer = DistanceCalculation.distance(pickUpAddress.getLatitude(),pickUpAddress.getLongitude(),+
                deliveryAddresses.getLatitude(),deliveryAddresses.getLongitude());
        String format = String.format("%.1f",calculatePrice(address,kilometer));

        return new ApiResponse<>(Double.parseDouble(format),true);
    }


    @Override
    public ApiResponse<List<LogisticCompany>> searchForAvailableLogistic() {
       List<LogisticCompany> availableLogisticCompany = logisticsService.findAvailableLogisticCompany();
       if(availableLogisticCompany.isEmpty())throw new NoAvailableException("No Logistic company Available");
       return new ApiResponse<>(availableLogisticCompany,true);

    }

    @Override
    public ApiResponse<?> cancelBookedDelivery(CustomerCancelBookingRequest cancelBookingRequest) {
      userExist(cancelBookingRequest.getCustomerEmail());
        Delivery delivery = deliveryService.cancelDelivery(cancelBookingRequest.getBookingId(),cancelBookingRequest.getCustomerEmail());
        if(delivery.getCompany() !=null) {
            LogisticCompany logisticCompany = logisticsService.updateLimitPerDay(cancelBookingRequest.getCompanyName(), cancelBookingRequest.getBookingId(),delivery.getNameOfVechicle());
            administratorService.cancelBookingEmail(logisticCompany.getEmail(),cancelBookingRequest.getBookingId(),cancelBookingRequest.getReasonOnWhyBookingWasCancelled(),
                    cancelBookingRequest.getCustomerEmail(), delivery.getDeliveryPrice());
        }
        return new ApiResponse<>("cancelled successfully",true);
       // else walletService.refundBalance(cancelBookingRequest.getCustomerEmail(), delivery.getDeliveryPrice());

    }

    @Override
    public Delivery findDeliveryById(FindABookedDeliveryRequest findABookedDeliveryRequest) {
        userExist(findABookedDeliveryRequest.getCustomerEmail())
                .orElseThrow(()->new UserExistException(String.format(USER_NOT_EXIST,findABookedDeliveryRequest.getCustomerEmail())));
        Delivery delivery = deliveryService.searchByDeliveryId(findABookedDeliveryRequest.getBookingId(), findABookedDeliveryRequest.getCustomerEmail());
        if(delivery == null)throw new DeliveryException("booking id doesn't exist");
        return delivery;
    }

    @Override
    public ApiResponse<String> trackOrder(TrackOrderRequest trackOrderRequest) {
        String status = deliveryService.getOrderStatus(trackOrderRequest.getBookingId());
        if(status == null)throw new DeliveryException("Order doesn't exist");
        return new ApiResponse<>(status,true);
    }

    @Override
    public ApiResponse<List<Delivery>> searchByDeliveryStatus(FindDeliveryByStatus findDeliveryByStatus) {
        userExist(findDeliveryByStatus.getEmail())
                .orElseThrow(()->new UserExistException(String.format(USER_NOT_EXIST,findDeliveryByStatus.getEmail())));
        List<Delivery> deliveries = deliveryService.searchDeliveryByStatus(findDeliveryByStatus.getDeliveryStatus(),findDeliveryByStatus.getEmail());
        if(deliveries.isEmpty())throw new DeliveryException("No delivery with the status");
        return new ApiResponse<>(deliveries,true);
    }

    @Override
    public List<Delivery> findAllDeliveries(String email) {
        userExist(email);
        List<Delivery> deliveries = deliveryService.findAllCustomerDelivery(email);
        if(deliveries.isEmpty())throw new NoDeliveryException("No delivery History");
        return deliveries;
    }

    @Override
    public void updateProfile(UpdateProfileRequest updateProfileRequest) {
        if(!Verification.verifyPhoneNumber(updateProfileRequest.getPhoneNumber()))throw new InvalidPhoneNumberException(INVALID_NUMBER);
        Customers customer = customerRepository.findByEmail(updateProfileRequest.getEmail())
                .orElseThrow(()->new UserExistException(String.format(USER_NOT_EXIST,updateProfileRequest)));
        List<JsonPatchOperation> operations = new ArrayList<>();
        createOperations(updateProfileRequest, operations);
        customer = apply(operations,customer);
        customerRepository.save(customer);
    }


    @Override
    public ApiResponse<?> setUpAddress(SetUpAddressRequest request) {
        Optional<Customers> customers = customerRepository.findByEmail(request.getEmail());
        return new ApiResponse<>
                (addressService.setUpAddress(request,customers.orElseThrow(()->
                        new UserExistException(String.format(USER_NOT_EXIST,request.getEmail())))),true);



    }

    private static void createOperations(UpdateProfileRequest request, List<JsonPatchOperation> operations) {
        Arrays.stream(request
                        .getClass().getDeclaredFields()).filter((x)->checkFields(request,x))
                .forEach((x)->operations.add(filterOperations(request,x)));
    }

    private static boolean checkFields(UpdateProfileRequest request,Field field){
        field.setAccessible(true);
        try {
            return field.get(request) != null &&(!field.getName().equals("email"));
        }catch (IllegalAccessException exception){
            throw  new RuntimeException();}
    }
    private static JsonPatchOperation filterOperations(UpdateProfileRequest request,Field field){
        try{
            JsonPointer path = new JsonPointer("/"+field.getName());
            JsonNode value = new TextNode(field.get(request).toString());
            return new ReplaceOperation(path,value);
        }
        catch (Exception exception){
            throw new RuntimeException();
        }
    }

    private static Customers apply(List<JsonPatchOperation>operations,Customers customers){
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.convertValue(customers, JsonNode.class);
            JsonPatch patch = new JsonPatch(operations);
            JsonNode applied = patch.apply(jsonNode);
            customers = mapper.convertValue(applied, Customers.class);
            return customers;
        }
        catch (JsonPatchException exception){
            throw new RuntimeException();
        }
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


    private static void checkIfRegisterRequestIsNull(CustomersRegisterRequest registerRequest) {
        if( registerRequest.getEmail() == null && registerRequest.getPassword() == null)throw new InvalidPasswordException("Kindly input correct details");
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println(email);
       Customers customers = userExist(email)
               .orElseThrow(()->new UserExistException(String.format(USER_NOT_EXIST,email)));
        return new UserConfig(customers);
    }
}
