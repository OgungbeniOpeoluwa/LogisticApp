package org.example.dto.request;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String name;
    private String city;
    private String street;
    private String country;
    private String phoneNumber;
    private String email;
}
