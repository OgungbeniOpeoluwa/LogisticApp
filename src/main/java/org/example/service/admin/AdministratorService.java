package org.example.service.admin;

import java.math.BigDecimal;

public interface AdministratorService {
    void depositConfirmationEmail(String email, double amount);

    void updateWallet(String email);
}
