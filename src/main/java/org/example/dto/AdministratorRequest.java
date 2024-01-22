package org.example.dto;

import lombok.Data;

@Data
public class AdministratorRequest {
    private  String name;
    private String phoneNumber;
    private String password;
    private String companyName;
    private String address;
    private String email;
}
