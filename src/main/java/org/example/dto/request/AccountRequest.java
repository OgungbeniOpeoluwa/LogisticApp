package org.example.dto.request;

import lombok.Data;

@Data
public class AccountRequest {
    private String username;
    private String accountNumber;
    private String accountName;
    private String bankName;
}
