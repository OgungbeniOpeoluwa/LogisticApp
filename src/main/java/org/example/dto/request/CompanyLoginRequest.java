package org.example.dto.request;

import lombok.Data;

@Data
public class CompanyLoginRequest {
    private String companyName;
    private String password;
}
