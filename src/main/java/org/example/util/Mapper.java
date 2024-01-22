package org.example.util;

import org.example.data.model.Customers;
import org.example.dto.EmailRequest;
import org.example.dto.RegisterRequest;
import org.example.exception.InvalidEmailException;
import org.example.exception.InvalidPasswordException;
import org.example.exception.InvalidPhoneNumberException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Mapper {

    public static Customers MapRegister(RegisterRequest request){
        if(!Verification.verifyPassword(request.getPassword()))throw new InvalidPasswordException("Weak password");
        if(!Verification.verifyEmail(request.getEmail()))throw new InvalidEmailException("Email not Valid");
        if(!Verification.verifyPhoneNumber(request.getPhoneNumber()))throw new InvalidPhoneNumberException("Enter a valid number");
        Customers customers = new Customers();
        customers.setName(request.getName());
        customers.setAddress(request.getAddress());
        customers.setEmail(request.getEmail());
        customers.setPhoneNumber(request.getPhoneNumber());
        String encodePassword = encryptPassword(request);
        customers.setPassword(encodePassword);
        return customers;

    }

    public static EmailRequest emailRequest(String email,String body,String title){
        EmailRequest emailRequest = new EmailRequest(email,body,title);
        return emailRequest;
    }

    private static String encryptPassword(RegisterRequest registerRequest) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodePassword = bCryptPasswordEncoder.encode(registerRequest.getPassword());
        return encodePassword;
    }
}
