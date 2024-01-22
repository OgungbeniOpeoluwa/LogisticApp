package org.example.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private  String name;
    private String phoneNumber;
    private String password;
    private String address;
    private String email;
}
