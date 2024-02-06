package org.example.service.wallet;

import org.example.data.model.Customers;
import org.example.data.model.Wallet;

import java.math.BigDecimal;

public interface WalletService {
    Wallet createWallet(Customers customers,String email);
    BigDecimal checkBalance(String customerEmail);

    Wallet depositMoney(double amount, String email);

    void updateWallet(String email);
}
