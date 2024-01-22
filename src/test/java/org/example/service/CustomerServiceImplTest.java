package org.example.service;

import org.example.data.repository.CustomerRepository;
import org.example.dto.LoginRequest;
import org.example.dto.RegisterRequest;
import org.example.exception.InvalidEmailException;
import org.example.exception.InvalidLoginDetail;
import org.example.exception.InvalidPasswordException;
import org.example.exception.InvalidPhoneNumberException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@TestPropertySource(locations = "/test.properties")
class CustomerServiceImplTest {
    @Autowired
    CustomerService customerService;

    @Autowired
    CustomerRepository customerRepository;
    @AfterEach
    public  void  deleteRepositoryAfterEach(){
        customerRepository.deleteAll();
    }

    @Test
    public void testThatWhenCustomerRegisterWithWrongPasswordFormatItThrowsException(){
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setName("opeoluwa");
        registerRequest.setEmail("ope@errt");
        registerRequest.setPassword("1234");
        registerRequest.setPhoneNumber("09084678536");
        registerRequest.setAddress("10 yaba lagos");
        assertThrows(InvalidPasswordException.class,()->customerService.register(registerRequest));
    }
    @Test
    public void testThatWhenUserRegisterWithAWrongEmailFormatThrowsAnException(){
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setName("opeoluwa");
        registerRequest.setEmail("ope@errt");
        registerRequest.setPassword("Opemip@123");
        registerRequest.setPhoneNumber("09084678536");
        registerRequest.setAddress("10 yaba lagos");
        assertThrows(InvalidEmailException.class,()->customerService.register(registerRequest));
    }
    @Test
    public void testThatWhenUserRegisterWithAWrongPhoneNumberItThrowsAnException(){
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setName("opeoluwa");
        registerRequest.setEmail("ope@gmail.com");
        registerRequest.setPassword("Opemip@123");
        registerRequest.setPhoneNumber("090846785362345");
        registerRequest.setAddress("10 yaba lagos");
        assertThrows(InvalidPhoneNumberException.class,()->customerService.register(registerRequest));
    }
    @Test
    public void testThatWhenAUserSuccessfullyRegisterItReturnsAMessageOfSuccessful(){
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setName("opeoluwa");
        registerRequest.setEmail("opeoluwaagnes@gmail.com");
        registerRequest.setPassword("Opemip@123");
        registerRequest.setPhoneNumber("07066221008");
        registerRequest.setAddress("10 yaba lagos");
        assertEquals("Registration completed",customerService.register(registerRequest).getMessage());
    }
    @Test
    public void testThatWhenUserLoginInWithWrongPasswordThrowsException(){
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setName("opeoluwa");
        registerRequest.setEmail("opeoluwaagnes@gmail.com");
        registerRequest.setPassword("Opemip@123");
        registerRequest.setPhoneNumber("07066221008");
        registerRequest.setAddress("10 yaba lagos");
        customerService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest("opeoluwaagnes@gmail.com","0pemip@23");
        assertThrows(InvalidLoginDetail.class,()->customerService.login(loginRequest));
    }
    @Test
    public void testThatWhenUserLoginInWithWrongEmailThrowsException(){
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setName("opeoluwa");
        registerRequest.setEmail("opeoluwaagnes@gmail.com");
        registerRequest.setPassword("Opemip@123");
        registerRequest.setPhoneNumber("07066221008");
        registerRequest.setAddress("10 yaba lagos");
        LoginRequest loginRequest = new LoginRequest("agnes@gmail.com","0pemip@123");
        assertThrows(InvalidLoginDetail.class,()->customerService.login(loginRequest));
    }


}