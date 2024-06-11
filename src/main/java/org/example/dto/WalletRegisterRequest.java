package org.example.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class WalletRegisterRequest {
    private String username;
    private String password;
    private String email;
    private String wallet_pin;
}
