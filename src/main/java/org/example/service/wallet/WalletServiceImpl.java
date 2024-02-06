package org.example.service.wallet;

import org.example.data.model.Customers;
import org.example.data.model.Wallet;
import org.example.data.model.WalletStatus;
import org.example.data.repository.WalletRepository;
import org.example.exception.WalletException;
import org.example.service.admin.AdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class WalletServiceImpl implements WalletService {
    @Autowired
    WalletRepository walletRepository;


    @Override
    public Wallet createWallet(Customers customer, String email) {
        Wallet wallet = new Wallet();
        wallet.setCustomers(customer);
        wallet.setEmail(email);
        wallet.setBalance(BigDecimal.valueOf(0));
        walletRepository.save(wallet);
        return wallet;
    }

    @Override
    public BigDecimal checkBalance(String customerEmail) {
        Optional<Wallet> wallet = walletRepository.findByEmail(customerEmail);
        if(wallet.isEmpty())throw new WalletException("Wallet doesn't exist");
        if(wallet.get().getWalletStatus() == WalletStatus.PENDING)throw new WalletException("Balance is currently not available");
        return wallet.get().getBalance();
    }

    @Override
    public Wallet depositMoney(double amount, String email) {
        Optional<Wallet> wallet = walletRepository.findByEmail(email);
        if(wallet.isEmpty())throw new WalletException("Wallet doesn't exist");
        double getBalance = Double.parseDouble(String.valueOf(wallet.get().getBalance()));
        double balance = getBalance + amount;
        wallet.get().setBalance(BigDecimal.valueOf(balance));
        wallet.get().setWalletStatus(WalletStatus.PENDING);
        walletRepository.save(wallet.get());
        return wallet.get();
    }

    @Override
    public void updateWallet(String email) {
        Optional<Wallet> wallet = walletRepository.findByEmail(email);
        if(wallet.isEmpty())throw new WalletException("Wallet doesn't exist");
        wallet.get().setWalletStatus(WalletStatus.AVAILABLE);
        walletRepository.save(wallet.get());
    }
}
