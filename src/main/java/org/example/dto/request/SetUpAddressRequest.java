package org.example.dto.request;

import lombok.Data;

@Data
public class SetUpAddressRequest {
    private String city;
    private String state;
    private String street;
    private String email;
}
