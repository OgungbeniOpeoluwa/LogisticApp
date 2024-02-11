package org.example.data.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private BigDecimal balance = BigDecimal.valueOf(0.00);
    @OneToOne(mappedBy = "wallet")
    private  Customers customers;
    @Enumerated
    private WalletStatus walletStatus = WalletStatus.AVAILABLE;
}
