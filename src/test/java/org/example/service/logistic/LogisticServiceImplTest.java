package org.example.service.logistic;

import jakarta.transaction.Transactional;
import org.example.data.model.Transaction;
import org.example.data.model.Vechicle;
import org.example.data.repository.CompanyRepository;
import org.example.data.repository.VechicleRepository;
import org.example.dto.request.BookDeliveryRequest;
import org.example.dto.request.*;
import org.example.data.CompanieDelivery;
import org.example.exception.InvalidLoginDetail;
import org.example.service.admin.AdministratorService;
import org.example.service.customers.CustomerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@TestPropertySource(locations = "/test.properties")
class LogisticServiceImplTest {
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    LogisticsService logisticsService;
    @Autowired
    VechicleRepository vechicleRepository;
    @Autowired
    CustomerService customerService;
    @Autowired
    AdministratorService administratorService;
    @AfterEach
    public void doThisAfterAll(){
//       companyRepository.deleteAll();
//       vechicleRepository.deleteAll();

    }
    @Test
    public void testThatWhenCompanyRegisterCountIsOne(){
        LogisticRegisterRequest registerRequest = new LogisticRegisterRequest();
        registerRequest.setCompanyName("Vision five Company");
        registerRequest.setPassword("Opemip@1");
        registerRequest.setAddress("10 gbagada estate");
        registerRequest.setEmail("ogunbeniopemipo1@gmail.com");
        registerRequest.setCacNumber("Ba123h565");
        registerRequest.setPhoneNumber("07066221006");
        logisticsService.register(registerRequest);
        assertEquals(1,companyRepository.count());

    }
    @Test
    public void testThatWhenUserLoginWithWrongPasswordThrowsException(){
        LogisticRegisterRequest registerRequest = new LogisticRegisterRequest();
        registerRequest.setCompanyName("Vision five Company");
        registerRequest.setPassword("Opemip@1");
        registerRequest.setAddress("10 gbagada estate");
        registerRequest.setEmail("ogunbeniopemipo1@gmail.com");
        registerRequest.setCacNumber("Ba123h565");
        registerRequest.setPhoneNumber("07066221006");
        logisticsService.register(registerRequest);

        CompanyLoginRequest companyLoginRequest = new CompanyLoginRequest();
        companyLoginRequest.setCompanyName(registerRequest.getCompanyName());
        companyLoginRequest.setPassword("12444");
        assertThrows(InvalidLoginDetail.class,()->logisticsService.login(companyLoginRequest));
    }
    @Test
    public void testThatWhenCompanyLoginWithWrongCompanyNameThrowsException(){
        LogisticRegisterRequest registerRequest = new LogisticRegisterRequest();
        registerRequest.setCompanyName("Vision five Company");
        registerRequest.setPassword("Opemip@1");
        registerRequest.setAddress("10 gbagada estate");
        registerRequest.setEmail("ogunbeniopemipo1@gmail.com");
        registerRequest.setCacNumber("Ba123h565");
        registerRequest.setPhoneNumber("07066221006");
        logisticsService.register(registerRequest);

        CompanyLoginRequest companyLoginRequest = new CompanyLoginRequest();
        companyLoginRequest.setCompanyName("Shola company");
        companyLoginRequest.setPassword(registerRequest.getPassword());
        assertThrows(InvalidLoginDetail.class,()->logisticsService.login(companyLoginRequest));

    }
    @Test
    public void testThatWhenCompanyCanSetUp2VechiclesAndIfIndAllVechicleOfCompanyCountIsTwo(){
        LogisticRegisterRequest registerRequest = new LogisticRegisterRequest();
        registerRequest.setCompanyName("Vision five Company");
        registerRequest.setPassword("Opemip@1");
        registerRequest.setAddress("10 gbagada estate");
        registerRequest.setEmail("ogunbeniopemipo1@gmail.com");
        registerRequest.setCacNumber("Ba123h565");
        registerRequest.setPhoneNumber("07066221006");
        logisticsService.register(registerRequest);

        CompanyLoginRequest companyLoginRequest = new CompanyLoginRequest();
        companyLoginRequest.setCompanyName(registerRequest.getCompanyName());
        companyLoginRequest.setPassword(registerRequest.getPassword());
        logisticsService.login(companyLoginRequest);


        RegisterVehicleRequest vechicleRequest = new RegisterVehicleRequest();
        vechicleRequest.setVehicleType("Bike");
        vechicleRequest.setCompanyName(registerRequest.getCompanyName());
        logisticsService.registerVechicle(vechicleRequest);

        RegisterVehicleRequest vechicleRequest2 = new RegisterVehicleRequest();
        vechicleRequest2.setVehicleType("Car");
        vechicleRequest2.setCompanyName(registerRequest.getCompanyName());
        logisticsService.registerVechicle(vechicleRequest2);

        List<Vechicle> allVechicle = logisticsService.findAllVechicle(registerRequest.getCompanyName());
        assertEquals(2,allVechicle.size());

    }
    @Test
    public void testThatLogistiscHasOneDeliveryaAndItWasCanceledTherListOfDeleviesBecomes0(){
        LogisticRegisterRequest registerRequest = new LogisticRegisterRequest();
        registerRequest.setCompanyName("Vision Five Company");
        registerRequest.setPassword("Opemip@1");
        registerRequest.setAddress("10 gbagada estate");
        registerRequest.setEmail("ogunbeniopemipo1@gmail.com");
        registerRequest.setCacNumber("Ba123h565");
        registerRequest.setPhoneNumber("07066221006");
        logisticsService.register(registerRequest);

        CompanyLoginRequest companyLoginRequest = new CompanyLoginRequest();
        companyLoginRequest.setCompanyName(registerRequest.getCompanyName());
        companyLoginRequest.setPassword(registerRequest.getPassword());
        logisticsService.login(companyLoginRequest);


        RegisterVehicleRequest vechicleRequest = new RegisterVehicleRequest();
        vechicleRequest.setVehicleType("Bike");
        vechicleRequest.setCompanyName(registerRequest.getCompanyName());
        logisticsService.registerVechicle(vechicleRequest);

        SetDayAvailabiltyRequest availabiltyRequest = new SetDayAvailabiltyRequest();
        availabiltyRequest.setNumber(5);
        availabiltyRequest.setVechicleType("bike");
        availabiltyRequest.setCompanyName(registerRequest.getCompanyName());

        logisticsService.setDayAvailability(availabiltyRequest);

        CustomersRegisterRequest customersRegisterRequest= new CustomersRegisterRequest();
        customersRegisterRequest.setEmail("opeoluwaagnes@gmail.com");
        customersRegisterRequest.setPassword("Opemip@123");
        customerService.register(customersRegisterRequest);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(customersRegisterRequest.getEmail());
        loginRequest.setPassword(customersRegisterRequest.getPassword());
        customerService.login(loginRequest);

        DepositMoneyRequest depositMoneyRequest = new DepositMoneyRequest();
        depositMoneyRequest.setEmail(customersRegisterRequest.getEmail());
        depositMoneyRequest.setAmount(60000);

        BookDeliveryRequest bookDeliveryRequest = bookDelivery();
        String bookingId = customerService.bookDelivery(bookDeliveryRequest);

        AcceptBookingRequest acceptBookingRequest = new AcceptBookingRequest();
        acceptBookingRequest.setCompanyName(registerRequest.getCompanyName());
        acceptBookingRequest.setBookingId(bookingId);
        acceptBookingRequest.setResponse("Accepted");
        logisticsService.responseToBookingRequest(acceptBookingRequest);

       BookDeliveryRequest bookDeliveryRequest2 = bookDelivery();
        String bookId = customerService.bookDelivery(bookDeliveryRequest2);

        acceptBookingRequest.setCompanyName(registerRequest.getCompanyName());
        acceptBookingRequest.setBookingId(bookId);
        acceptBookingRequest.setResponse("Accepted");
        logisticsService.responseToBookingRequest(acceptBookingRequest);

        CancelBookingRequest cancelBookingRequest = new CancelBookingRequest();
        cancelBookingRequest.setReasonForCancellation("Bike break down");
        cancelBookingRequest.setCompanyName("Vision Five Company");
        cancelBookingRequest.setBookingId(bookingId);
        cancelBookingRequest.setCustomerEmail(customersRegisterRequest.getEmail());
        logisticsService.cancelDelivery(cancelBookingRequest);

        SearchByDeliveryStatusRequest deliveryStatusRequest = new SearchByDeliveryStatusRequest();
        deliveryStatusRequest.setCompanyName(registerRequest.getCompanyName());
        deliveryStatusRequest.setDeliveryStatus("cancelled");


        List<CompanieDelivery> companyDeliveries = logisticsService.searchBydeliveryStatus(deliveryStatusRequest);
        assertEquals(1,companyDeliveries.size());

    }
    @Test
    public  void testThatLogisticCompanyCanUpdateDeliveryStatus(){
        LogisticRegisterRequest registerRequest = new LogisticRegisterRequest();
        registerRequest.setCompanyName("Vision Five Company");
        registerRequest.setPassword("Opemip@1");
        registerRequest.setAddress("10 gbagada estate");
        registerRequest.setEmail("ogunbeniopemipo1@gmail.com");
        registerRequest.setCacNumber("Ba123h565");
        registerRequest.setPhoneNumber("07066221006");
        logisticsService.register(registerRequest);

        CompanyLoginRequest companyLoginRequest = new CompanyLoginRequest();
        companyLoginRequest.setCompanyName(registerRequest.getCompanyName());
        companyLoginRequest.setPassword(registerRequest.getPassword());
        logisticsService.login(companyLoginRequest);


        RegisterVehicleRequest vechicleRequest = new RegisterVehicleRequest();
        vechicleRequest.setVehicleType("Bike");
        vechicleRequest.setCompanyName(registerRequest.getCompanyName());
        logisticsService.registerVechicle(vechicleRequest);

        SetDayAvailabiltyRequest availabiltyRequest = new SetDayAvailabiltyRequest();
        availabiltyRequest.setNumber(5);
        availabiltyRequest.setVechicleType("bike");
        availabiltyRequest.setCompanyName(registerRequest.getCompanyName());

        logisticsService.setDayAvailability(availabiltyRequest);

        CustomersRegisterRequest customersRegisterRequest= new CustomersRegisterRequest();
        customersRegisterRequest.setEmail("opeoluwaagnes@gmail.com");
        customersRegisterRequest.setPassword("Opemip@123");
        customerService.register(customersRegisterRequest);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(customersRegisterRequest.getEmail());
        loginRequest.setPassword(customersRegisterRequest.getPassword());
        customerService.login(loginRequest);

        DepositMoneyRequest depositMoneyRequest = new DepositMoneyRequest();
        depositMoneyRequest.setEmail(customersRegisterRequest.getEmail());
        depositMoneyRequest.setAmount(60000);

        BookDeliveryRequest bookDeliveryRequest = bookDelivery();
        String bookingId = customerService.bookDelivery(bookDeliveryRequest);

        AcceptBookingRequest acceptBookingRequest = new AcceptBookingRequest();
        acceptBookingRequest.setCompanyName(registerRequest.getCompanyName());
        acceptBookingRequest.setBookingId(bookingId);
        acceptBookingRequest.setResponse("Accepted");
        logisticsService.responseToBookingRequest(acceptBookingRequest);

        UpdateDeliveryStatusRequest updateDeliveryStatusRequest = new UpdateDeliveryStatusRequest();
        updateDeliveryStatusRequest.setCompanyName("Vision Five Company");
        updateDeliveryStatusRequest.setBookingId(bookingId);
        updateDeliveryStatusRequest.setUpdate("In_Transit");

        logisticsService.updateDeliveryStatus(updateDeliveryStatusRequest);

        TrackOrderRequest trackOrderRequest = new TrackOrderRequest();
        trackOrderRequest.setBookingId(bookingId);

        assertEquals("IN_TRANSIT", customerService.trackOrder(trackOrderRequest));

    }
    @Test
    public void testThatWhenLogisticCompanySaveBikeInVechicleAndTheyDeleteBikeFromList_ListIs0(){
        LogisticRegisterRequest registerRequest = new LogisticRegisterRequest();
        registerRequest.setCompanyName("Vision Five Company");
        registerRequest.setPassword("Opemip@1");
        registerRequest.setAddress("10 gbagada estate");
        registerRequest.setEmail("ogunbeniopemipo1@gmail.com");
        registerRequest.setCacNumber("Ba123h565");
        registerRequest.setPhoneNumber("07066221006");
        logisticsService.register(registerRequest);

        CompanyLoginRequest companyLoginRequest = new CompanyLoginRequest();
        companyLoginRequest.setCompanyName(registerRequest.getCompanyName());
        companyLoginRequest.setPassword(registerRequest.getPassword());
        logisticsService.login(companyLoginRequest);


        RegisterVehicleRequest vechicleRequest = new RegisterVehicleRequest();
        vechicleRequest.setVehicleType("Bike");
        vechicleRequest.setCompanyName(registerRequest.getCompanyName());
        logisticsService.registerVechicle(vechicleRequest);

        DeleteVechicleRequest deleteVechicleRequest = new DeleteVechicleRequest();
        deleteVechicleRequest.setVechicleType("bike");
        deleteVechicleRequest.setCompanyName("Vision Five Company");
         logisticsService.deleteVechicle(deleteVechicleRequest);

         List<Vechicle> allVechicle = logisticsService.findAllVechicle(registerRequest.getCompanyName());
         assertEquals(0,allVechicle.size());


    }
    @Test
    public void testThatWhenLogistCompanyFindALLTransactionReturnListOfTransactionsOfDeliveryThatHasBeenDone(){
        LogisticRegisterRequest registerRequest = new LogisticRegisterRequest();
        registerRequest.setCompanyName("Vision Five Company");
        registerRequest.setPassword("Opemip@1");
        registerRequest.setAddress("10 gbagada estate");
        registerRequest.setEmail("ogunbeniopemipo1@gmail.com");
        registerRequest.setCacNumber("Ba123h565");
        registerRequest.setPhoneNumber("07066221006");
        logisticsService.register(registerRequest);

        CompanyLoginRequest companyLoginRequest = new CompanyLoginRequest();
        companyLoginRequest.setCompanyName(registerRequest.getCompanyName());
        companyLoginRequest.setPassword(registerRequest.getPassword());
        logisticsService.login(companyLoginRequest);


        RegisterVehicleRequest vechicleRequest = new RegisterVehicleRequest();
        vechicleRequest.setVehicleType("Bike");
        vechicleRequest.setCompanyName(registerRequest.getCompanyName());
        logisticsService.registerVechicle(vechicleRequest);

        SetDayAvailabiltyRequest availabiltyRequest = new SetDayAvailabiltyRequest();
        availabiltyRequest.setNumber(5);
        availabiltyRequest.setVechicleType("bike");
        availabiltyRequest.setCompanyName(registerRequest.getCompanyName());

        logisticsService.setDayAvailability(availabiltyRequest);

        CustomersRegisterRequest customersRegisterRequest= new CustomersRegisterRequest();
        customersRegisterRequest.setEmail("opeoluwaagnes@gmail.com");
        customersRegisterRequest.setPassword("Opemip@123");
        customerService.register(customersRegisterRequest);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(customersRegisterRequest.getEmail());
        loginRequest.setPassword(customersRegisterRequest.getPassword());
        customerService.login(loginRequest);

        DepositMoneyRequest depositMoneyRequest = new DepositMoneyRequest();
        depositMoneyRequest.setEmail(customersRegisterRequest.getEmail());
        depositMoneyRequest.setAmount(60000);

        BookDeliveryRequest bookDeliveryRequest = bookDelivery();
        String bookingId = customerService.bookDelivery(bookDeliveryRequest);

        AcceptBookingRequest acceptBookingRequest = new AcceptBookingRequest();
        acceptBookingRequest.setCompanyName(registerRequest.getCompanyName());
        acceptBookingRequest.setBookingId(bookingId);
        acceptBookingRequest.setResponse("Accepted");
        logisticsService.responseToBookingRequest(acceptBookingRequest);

        BookDeliveryRequest bookDeliveryRequest2 = bookDelivery();
        String bookId = customerService.bookDelivery(bookDeliveryRequest2);

        acceptBookingRequest.setCompanyName(registerRequest.getCompanyName());
        acceptBookingRequest.setBookingId(bookId);
        acceptBookingRequest.setResponse("Accepted");
        logisticsService.responseToBookingRequest(acceptBookingRequest);

        UpdateDeliveryStatusRequest updateDeliveryStatusRequest = new UpdateDeliveryStatusRequest();
        updateDeliveryStatusRequest.setCompanyName("Vision Five Company");
        updateDeliveryStatusRequest.setBookingId(bookingId);
        updateDeliveryStatusRequest.setUpdate("Delivered");

        logisticsService.updateDeliveryStatus(updateDeliveryStatusRequest);

        GetTransactionRequest getTransactionRequest = new GetTransactionRequest();
        getTransactionRequest.setCompanyName("Vision Five Company");
        getTransactionRequest.setEmail("adefiranyegbolaga@gmail.com");


        List<Transaction> allTransaction = logisticsService.getTransactions(getTransactionRequest);
        assertEquals(1,allTransaction.size());


    }

    private static BookDeliveryRequest bookDelivery() {
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

        bookDeliveryRequest.setLogisticCompanyName("Vision Five Company");


        bookDeliveryRequest.setTypeOfPackage("files");
        bookDeliveryRequest.setPackageWeight(5);
        bookDeliveryRequest.setTypeOfVechicle("bike");
        return bookDeliveryRequest;
    }



}