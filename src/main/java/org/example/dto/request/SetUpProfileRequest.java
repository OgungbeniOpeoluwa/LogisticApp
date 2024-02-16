package org.example.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SetUpProfileRequest {
    private String email;
    @NotNull
    private  String name;
    @NotNull
    private String phoneNumber;
    @NotNull
    private String city;
    @NotNull
    private  String street;
    @NotNull
    private String country;
}
