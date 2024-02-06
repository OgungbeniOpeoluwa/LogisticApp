package org.example.dto.request;

import lombok.Data;

@Data
public class LogisticRegisterRequest {
    private String phoneNumber;
    private String password;
    private String companyName;
    private String address;
    private String email;
    private String cacNumber;
}
