package org.example.service;

import org.example.data.model.Wallet;
import org.example.data.model.WalletStatus;
import org.example.data.repository.CustomerRepository;
import org.example.data.repository.WalletRepository;
import org.example.dto.CheckPriceQuotationRequest;
import org.example.dto.BookDeliveryRequest;
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

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@TestPropertySource(locations = "/test.properties")
class CustomerServiceImplTest {
    @Autowired
    CustomerService customerService;

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    WalletRepository walletRepository;
    @Autowired
    AdministratorService administratorService;
    @Autowired
    LogisticsService logisticsService;


    private CustomersRegisterRequest customersRegisterRequest;
    @AfterEach
    public  void  deleteRepositoryAfterEach(){
        customerRepository.deleteAll();
        walletRepository.deleteAll();

    }
    @BeforeEach
    public void setUpRegistration(){
        customersRegisterRequest= new CustomersRegisterRequest();
        customersRegisterRequest.setName("opeoluwa");
        customersRegisterRequest.setEmail("opeoluwaagnes@gmail.com");
        customersRegisterRequest.setPassword("Opemip@123");
        customersRegisterRequest.setPhoneNumber("07066221008");
        customersRegisterRequest.setAddress("10 yaba lagos");

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
    public void testThatWhenUserRegisterWithAWrongPhoneNumberItThrowsAnException(){
        customersRegisterRequest.setPhoneNumber("090846785362345");
        assertThrows(InvalidPhoneNumberException.class,()->customerService.register(customersRegisterRequest));
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
        address.setCustomerEmail("opeoluwaagnes@gmail.com");
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
        BookDeliveryRequest bookDeliveryRequest = new BookDeliveryRequest();

        bookDeliveryRequest.setPickUpName("delighted");
        bookDeliveryRequest.setPickUpStreet("19 igbobi road adesan");
        bookDeliveryRequest.setPickUpCity("mowe");
        bookDeliveryRequest.setPickUpState("Ogun state");
        bookDeliveryRequest.setPickUpPhoneNumber("0706622108");

        bookDeliveryRequest.setCustomerEmail("opeoluwaagnes@gmail.com");

        bookDeliveryRequest.setReceiverCity("Akoka yaba");
        bookDeliveryRequest.setReceiverState("lagos state");
        bookDeliveryRequest.setReceiverStreet("13 Emily Akinsola");
        bookDeliveryRequest.setReceiverPhoneNumber("08152865402");
        bookDeliveryRequest.setReceiverName("shola");


        bookDeliveryRequest.setTypeOfPackage("files");
        bookDeliveryRequest.setPackageWeight(5);
        bookDeliveryRequest.setTypeOfVechicle("bike");
        assertThrows(InsufficientBalanceException.class,()->customerService.bookDelivery(bookDeliveryRequest));

    }
    @Test
    public void testThatWhenUserDeposit1500InsideTheirWalletWalletIncreaseFrom0To1500(){
        customerService.register(customersRegisterRequest);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(customersRegisterRequest.getEmail());
        loginRequest.setPassword(customersRegisterRequest.getPassword());
        customerService.login(loginRequest);

        DepositMoneyRequest depositMoneyRequest = new DepositMoneyRequest();
        depositMoneyRequest.setEmail(customersRegisterRequest.getEmail());
        depositMoneyRequest.setAmount(1500.00);

        customerService.depositToWallet(depositMoneyRequest);
        Optional<Wallet> wallet = walletRepository.findByEmail(customersRegisterRequest.getEmail());
        assertSame(WalletStatus.PENDING,wallet.get().getWalletStatus());

        assertThrows(WalletException.class,()->customerService.checkBalance(customersRegisterRequest.getEmail()));
        administratorService.updateWallet(customersRegisterRequest.getEmail());
        assertSame(WalletStatus.AVAILABLE,walletRepository.findByEmail(customersRegisterRequest.getEmail()).get().getWalletStatus());
        assertEquals(0,BigDecimal.valueOf(1500).compareTo(customerService.checkBalance(customersRegisterRequest.getEmail())));
        assertSame(WalletStatus.AVAILABLE,walletRepository.findByEmail(customersRegisterRequest.getEmail()).get().getWalletStatus());
    }
    @Test
    public void testThatWhenUserSearchForAvailableLogisticCompanyReturnsCompanyThatIsOnline(){
        customerService.register(customersRegisterRequest);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(customersRegisterRequest.getEmail());
        loginRequest.setPassword(customersRegisterRequest.getPassword());
        customerService.login(loginRequest);

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

        LogisticRegisterRequest registerRequest2 = new LogisticRegisterRequest();
        registerRequest2.setCompanyName("five Company");
        registerRequest2.setPassword("Opemip@1");
        registerRequest2.setAddress("10 mowe estate");
        registerRequest2.setEmail("ope@gmail.com");
        registerRequest2.setCacNumber("Ba123h534re");
        registerRequest2.setPhoneNumber("081528654");
        logisticsService.register(registerRequest2);



    }


}