package org.example.data.repository;

import org.example.data.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository  extends JpaRepository<Wallet,Long> {
    Optional<Wallet> findByEmail(String customerEmail);
}