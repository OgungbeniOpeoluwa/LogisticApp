package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.data.model.Customers;
import org.example.data.repository.CustomerRepository;
import org.example.dto.EmailRequest;
import org.example.dto.LoginRequest;
import org.example.dto.RegisterRequest;
import org.example.dto.RegisterResponse;
import org.example.exception.ActionDoneException;
import org.example.exception.InvalidLoginDetail;
import org.example.exception.InvalidPasswordException;
import org.example.exception.UserExistException;
import org.example.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    UserService userService;
    @Autowired
    EmailService emailService;


    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {
        Customers customers = customerRepository.findByEmail(registerRequest.getEmail());
        checkIfRegisterRequestIsNull(registerRequest);
        if(customers != null)throw new UserExistException("User Already Exist");
        Customers customer = Mapper.MapRegister(registerRequest);
        customerRepository.save(customer);
        userService.save(customer);
      RegisterResponse response = new RegisterResponse("Registration completed");
        EmailRequest emailRequest = Mapper.emailRequest(registerRequest.getEmail(),
                "Congratulation "+registerRequest.getName()+" you have successfully register",
                "Registration Complete");
        emailService.send(emailRequest);
        return response;
    }

    @Override
    public Customers customer(String email) {
        Customers customers = customerRepository.findByEmail(email);
        return customers;
    }

    @Override
    public void login(LoginRequest loginRequest) {
        Customers customers = customerRepository.findByEmail(loginRequest.getEmail());
        if(customers== null)throw new InvalidLoginDetail("invalid details");
        if(customers.isEnable())throw new ActionDoneException("User has already login");
        if(!checkPassword(customers.getEmail(),loginRequest.getPassword())) throw new InvalidLoginDetail("invalid login details");
        customers.setEnable(true);
        customerRepository.save(customers);



    }
    private boolean checkPassword(String email,String password){
       Customers customers = customerRepository.findByEmail(email);
        BCryptPasswordEncoder decodePassword = new BCryptPasswordEncoder();
      return decodePassword.matches(password,customers.getPassword());
    }

    private static void checkIfRegisterRequestIsNull(RegisterRequest registerRequest) {
        if(registerRequest.getName() == null && registerRequest.getEmail() == null &&
                registerRequest.getPassword() == null && registerRequest.getAddress() == null &&
                registerRequest.getPhoneNumber() == null)throw new InvalidPasswordException("Kindly input correct details");
    }
}
