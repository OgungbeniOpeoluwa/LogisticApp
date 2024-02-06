package org.example.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CustomersRegisterRequest {
    @NotNull
    private  String name;
    @NotNull
    private String phoneNumber;
    @NotNull
    private String password;
    @NotNull
    private String address;
    @NotNull
    private String email;
}
