package org.example.data.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity

public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String customerEmail;
    private String bookingId;
    private LocalDateTime localDateTitme = LocalDateTime.now();
    @ManyToOne(cascade = {CascadeType.ALL},fetch = FetchType.LAZY)
    private LogisticCompany company;
    private BigDecimal amount;
}
