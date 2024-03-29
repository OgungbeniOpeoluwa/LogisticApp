package org.example.service;

import jakarta.transaction.Transactional;
import org.example.data.model.*;
import org.example.data.repository.*;
import org.example.dto.request.CheckPriceQuotationRequest;
import org.example.dto.request.BookDeliveryRequest;
import org.example.dto.request.*;
import org.example.exception.*;
import org.example.service.admin.AdministratorService;
import org.example.service.customers.CustomerService;
import org.example.service.logistic.LogisticsService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@TestPropertySource(locations = "/test.properties")
class CustomerServiceImplTest {
    @Autowired
    CustomerService customerService;

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    AdministratorService administratorService;
    @Autowired
    LogisticsService logisticsService;
    @Autowired
    VechicleRepository vechicleRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    DeliveryRepository deliveryRepository;


    private CustomersRegisterRequest customersRegisterRequest;
    @AfterEach
    public  void  deleteRepositoryAfterEach(){
        customerRepository.deleteAll();
        companyRepository.deleteAll();
        deliveryRepository.deleteAll();
        vechicleRepository.deleteAll();


    }
    @BeforeEach
    public void setUpRegistration(){
        customersRegisterRequest= new CustomersRegisterRequest();
        customersRegisterRequest.setEmail("opeoluwaagnes@gmail.com");
        customersRegisterRequest.setPassword("Opemip@123");
    }

    @Test
    public void testThatWhenCustomerRegisterWithWrongPasswordFormatItThrowsException(){;
        customersRegisterRequest.setPassword("1234");
        assertThrows(InvalidPasswordException.class,()->customerService.register(customersRegisterRequest));
    }
    @Test
    public void testThatWhenUserRegisterWithAWrongEmailFormatThrowsAnException(){
        customersRegisterRequest.setEmail("ope@errt");
        assertThrows(InvalidEmailException.class,()->customerService.register(customersRegisterRequest));
    }
    @Test
    public void testThatWhenAUserSuccessfullyRegisterItReturnsAMessageOfSuccessful(){
        assertEquals("Registration completed",customerService.register(customersRegisterRequest).getMessage());
    }
    @Test
    public void testThatWhenUserLoginInWithWrongPasswordThrowsException(){
        customerService.register(customersRegisterRequest);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("opeoluwaagnes@gmail.com");
        loginRequest.setPassword("0pemip@23");
        assertThrows(InvalidLoginDetail.class,()->customerService.login(loginRequest));
    }
    @Test
    public void testThatWhenUserLoginInWithWrongEmailThrowsException(){
        customerService.register(customersRegisterRequest);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("agnes@gmail.com");
        loginRequest.setPassword("0pemip@123");
        assertThrows(InvalidLoginDetail.class,()->customerService.login(loginRequest));
    }
    @Test
    public void testThatWhenUserLoginWithNoInputThrowsException(){
        customerService.register(customersRegisterRequest);
        LoginRequest loginRequest = new LoginRequest();
       assertThrows(InputException.class,()->customerService.login(loginRequest));
    }
    @Test
    public void testThatCustomersCanGetThePriceOfDeliveryBeforeBooking(){
        customerService.register(customersRegisterRequest);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(customersRegisterRequest.getEmail());
        loginRequest.setPassword(customersRegisterRequest.getPassword());
        customerService.login(loginRequest);
        CheckPriceQuotationRequest address = new CheckPriceQuotationRequest();
        address.setPickUpStreet("19 igbobi road adesan");
        address.setDeliveryState("lagos state");
        address.setDeliveryCity("akoka yaba");
        address.setPickUpState("ogun state");
        address.setPickUpCity("mowe");
        address.setDeliveryStreet("13 Emily akinsola");
        address.setTypeOfVehicle("bike");
        address.setWeightOfPackage(5);
        assertEquals(14620,customerService.getQuote(address));


    }
    @Test
    public void testThatWhenUserTriesToBookDeliveryWithInSufficientMoneyInWalletThrowsAnException(){
        customerService.register(customersRegisterRequest);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(customersRegisterRequest.getEmail());
        loginRequest.setPassword(customersRegisterRequest.getPassword());
        customerService.login(loginRequest);
        BookDeliveryRequest bookDeliveryRequest = bookDelivery();
        assertThrows(InsufficientBalanceException.class,()->customerService.bookDelivery(bookDeliveryRequest));

    }
    @Test
    public void testThatWhenUserSearchForAvailableLogisticCompanyReturnsCompanyThatIsOnline(){
        customerService.register(customersRegisterRequest);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(customersRegisterRequest.getEmail());
        loginRequest.setPassword(customersRegisterRequest.getPassword());
        customerService.login(loginRequest);

        LogisticRegisterRequest registerRequest = getLogisticRegisterRequest();
        logisticsService.register(registerRequest);

        CompanyLoginRequest companyLoginRequest = new CompanyLoginRequest();
        companyLoginRequest.setCompanyName(registerRequest.getCompanyName());
        companyLoginRequest.setPassword(registerRequest.getPassword());
        logisticsService.login(companyLoginRequest);

        LogisticRegisterRequest registerRequest2 = new LogisticRegisterRequest();
        registerRequest2.setCompanyName("five Company");
        registerRequest2.setPassword("Opemip@1");
        registerRequest2.setAddress("10 mowe estate");
        registerRequest2.setEmail("ope@gmail.com");
        registerRequest2.setCacNumber("Ba123h534re");
        registerRequest2.setPhoneNumber("08152865402");
        logisticsService.register(registerRequest2);

        List<LogisticCompany> availableLogisticCompany = customerService.searchForAvailableLogistic();
        System.out.println(availableLogisticCompany);
        assertEquals(1,availableLogisticCompany.size());

    }
    @Test
    public void testThatWhenCustomerBookDeliveryAndALogisticCompanyAvailabilityIs5ItReduceBy1AndAlsoMoneyIsDeductedFromCustomerWallet(){
        customerService.register(customersRegisterRequest);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(customersRegisterRequest.getEmail());
        loginRequest.setPassword(customersRegisterRequest.getPassword());
        customerService.login(loginRequest);

        LogisticRegisterRequest registerRequest = getLogisticRegisterRequest();
        logisticsService.register(registerRequest);

        CompanyLoginRequest companyLoginRequest = new CompanyLoginRequest();
        companyLoginRequest.setCompanyName(registerRequest.getCompanyName());
        companyLoginRequest.setPassword(registerRequest.getPassword());
        logisticsService.login(companyLoginRequest);

        RegisterVehicleRequest vechicleRequest = getRegisterVehicleRequest(registerRequest.getCompanyName());
        logisticsService.registerVechicle(vechicleRequest);

        SetDayAvailabiltyRequest availabiltyRequest = new SetDayAvailabiltyRequest();
        availabiltyRequest.setNumber(5);
        availabiltyRequest.setVechicleType("bike");
        availabiltyRequest.setCompanyName("Vision five company");

        logisticsService.setDayAvailability(availabiltyRequest);


        DepositMoneyRequest depositMoneyRequest = new DepositMoneyRequest();
        depositMoneyRequest.setEmail(customersRegisterRequest.getEmail());
        depositMoneyRequest.setAmount(20000);
        BookDeliveryRequest bookDeliveryRequest = bookDelivery();
        String bookingId = customerService.bookDelivery(bookDeliveryRequest);


        AcceptBookingRequest acceptBookingRequest = new AcceptBookingRequest();
        acceptBookingRequest.setCompanyName("Vision five company");
        acceptBookingRequest.setBookingId(bookingId);
        acceptBookingRequest.setResponse("Accepted");
        logisticsService.responseToBookingRequest(acceptBookingRequest);


        List<Vechicle> availability = logisticsService.findAllVechicle("Vision five company");
        int availaibility = availability.get(0).getLimitPerDay();
        assertEquals(4,availaibility);
    }
    @Test
    public void testThatWhenUserCancelRequestBookingStatusChangeToCanceledAndHerMoneyIsRefunded(){
        customerService.register(customersRegisterRequest);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(customersRegisterRequest.getEmail());
        loginRequest.setPassword(customersRegisterRequest.getPassword());
        customerService.login(loginRequest);

        LogisticRegisterRequest registerRequest = getLogisticRegisterRequest();
        logisticsService.register(registerRequest);

        CompanyLoginRequest companyLoginRequest = new CompanyLoginRequest();
        companyLoginRequest.setCompanyName(registerRequest.getCompanyName());
        companyLoginRequest.setPassword(registerRequest.getPassword());
        logisticsService.login(companyLoginRequest);

        RegisterVehicleRequest vechicleRequest = getRegisterVehicleRequest(registerRequest.getCompanyName());
        logisticsService.registerVechicle(vechicleRequest);

        SetDayAvailabiltyRequest availabiltyRequest = new SetDayAvailabiltyRequest();
        availabiltyRequest.setNumber(5);
        availabiltyRequest.setVechicleType("bike");
        availabiltyRequest.setCompanyName("Vision five company");

        logisticsService.setDayAvailability(availabiltyRequest);


        DepositMoneyRequest depositMoneyRequest = new DepositMoneyRequest();
        depositMoneyRequest.setEmail(customersRegisterRequest.getEmail());
        depositMoneyRequest.setAmount(20000);

        BookDeliveryRequest bookDeliveryRequest = bookDelivery();
        String bookingId = customerService.bookDelivery(bookDeliveryRequest);


        CustomerCancelBookingRequest cancelBookingRequest = new CustomerCancelBookingRequest();
        cancelBookingRequest.setBookingId(bookingId);
        cancelBookingRequest.setReasonOnWhyBookingWasCancelled("Delay In Delivery");
        cancelBookingRequest.setCompanyName(registerRequest.getCompanyName());
        cancelBookingRequest.setCustomerEmail(customersRegisterRequest.getEmail());
        customerService.cancelBookedDelivery(cancelBookingRequest);

        TrackOrderRequest trackOrderRequest = new TrackOrderRequest();
        trackOrderRequest.setBookingId(bookingId);
        String statues = customerService.trackOrder(trackOrderRequest);
        assertEquals("CANCELLED",statues);
    }
    @Test
    public void testThatCustomerCanFindDeliveryByDeliveryStatus(){
        customerService.register(customersRegisterRequest);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(customersRegisterRequest.getEmail());
        loginRequest.setPassword(customersRegisterRequest.getPassword());
        customerService.login(loginRequest);

        DepositMoneyRequest depositMoneyRequest = new DepositMoneyRequest();
        depositMoneyRequest.setEmail(customersRegisterRequest.getEmail());
        depositMoneyRequest.setAmount(20000);

        LogisticRegisterRequest registerRequest = getLogisticRegisterRequest();
        logisticsService.register(registerRequest);

        CompanyLoginRequest companyLoginRequest = new CompanyLoginRequest();
        companyLoginRequest.setCompanyName(registerRequest.getCompanyName());
        companyLoginRequest.setPassword(registerRequest.getPassword());
        logisticsService.login(companyLoginRequest);

        RegisterVehicleRequest vechicleRequest = getRegisterVehicleRequest(registerRequest.getCompanyName());
        logisticsService.registerVechicle(vechicleRequest);

        SetDayAvailabiltyRequest availabiltyRequest = new SetDayAvailabiltyRequest();
        availabiltyRequest.setNumber(5);
        availabiltyRequest.setVechicleType("bike");
        availabiltyRequest.setCompanyName("Vision five company");

        logisticsService.setDayAvailability(availabiltyRequest);


        BookDeliveryRequest bookDeliveryRequest = bookDelivery();
        String bookingId = customerService.bookDelivery(bookDeliveryRequest);

        FindDeliveryByStatus findDeliveryByStatus = new FindDeliveryByStatus();
        findDeliveryByStatus.setDeliveryStatus("Pending");
        findDeliveryByStatus.setEmail(customersRegisterRequest.getEmail());

        List<Delivery> deliveries = customerService.searchByDeliveryStatus(findDeliveryByStatus);
        assertEquals(1,deliveries.size());
    }
    @Test
    public void testThatWhenCustomerTriesToFindDeliveryWithoutRegisteringThrowsAnException(){
        customerService.register(customersRegisterRequest);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(customersRegisterRequest.getEmail());
        loginRequest.setPassword(customersRegisterRequest.getPassword());
        customerService.login(loginRequest);

        DepositMoneyRequest depositMoneyRequest = new DepositMoneyRequest();
        depositMoneyRequest.setEmail(customersRegisterRequest.getEmail());
        depositMoneyRequest.setAmount(20000);

        assertThrows(NoDeliveryException.class,()->customerService.findAllDeliveries(customersRegisterRequest.getEmail()));


    }
    @Test
    public void testThatCustomerUpdateUpdateTheirNameFromOpeoluwaToShayo(){
        customerService.register(customersRegisterRequest);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(customersRegisterRequest.getEmail());
        loginRequest.setPassword(customersRegisterRequest.getPassword());
        customerService.login(loginRequest);

        UpdateProfileRequest updateProfileRequest = new UpdateProfileRequest();
        updateProfileRequest.setName("Shayo");
        updateProfileRequest.setEmail(customersRegisterRequest.getEmail());
        customerService.updateProfile(updateProfileRequest);

        assertEquals("shayo",customerRepository.findByEmail(customersRegisterRequest.getEmail()));


    }

    private static  BookDeliveryRequest bookDelivery() {
        BookDeliveryRequest bookDeliveryRequest = new BookDeliveryRequest();
        bookDeliveryRequest.setPickUpName("delighted");
        bookDeliveryRequest.setPickUpStreet("19 igbobi road adesan");
        bookDeliveryRequest.setPickUpArea("mowe");
        bookDeliveryRequest.setPickUpState("Ogun state");
        bookDeliveryRequest.setPickUpPhoneNumber("0706622108");

        bookDeliveryRequest.setCustomerEmail("opeoluwaagnes@gmail.com");

        bookDeliveryRequest.setReceiverArea("Akoka yaba");
        bookDeliveryRequest.setReceiverState("lagos state");
        bookDeliveryRequest.setReceiverStreet("13 Emily Akinsola");
        bookDeliveryRequest.setReceiverPhoneNumber("08152865402");
        bookDeliveryRequest.setReceiverName("shola");

        bookDeliveryRequest.setLogisticCompanyName("Vision five Company");


        bookDeliveryRequest.setTypeOfPackage("files");
        bookDeliveryRequest.setPackageWeight(5);
        bookDeliveryRequest.setTypeOfVechicle("bike");
        return bookDeliveryRequest;
    }

    private static RegisterVehicleRequest getRegisterVehicleRequest(String companyName) {
        RegisterVehicleRequest vechicleRequest = new RegisterVehicleRequest();
        vechicleRequest.setVehicleType("Bike");
        vechicleRequest.setCompanyName(companyName);
        return vechicleRequest;
    }

    private static LogisticRegisterRequest getLogisticRegisterRequest() {
        LogisticRegisterRequest registerRequest = new LogisticRegisterRequest();
        registerRequest.setCompanyName("Vision five Company");
        registerRequest.setPassword("Opemip@1");
        registerRequest.setAddress("10 gbagada estate");
        registerRequest.setEmail("ogungbeniopemipo1@gmail.com");
        registerRequest.setCacNumber("Ba123h565");
        registerRequest.setPhoneNumber("07066221006");
        return registerRequest;
    }


}