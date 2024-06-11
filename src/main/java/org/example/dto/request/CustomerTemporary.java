package org.example.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomerTemporary {
    private String name;
    private String city;
    private String street;
    private String country;
    private String phoneNumber;
    private String email;
}
