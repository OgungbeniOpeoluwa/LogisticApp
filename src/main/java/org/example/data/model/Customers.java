package org.example.data.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
public class Customers{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String phoneNumber;
    private String password;
    private String name;
    private String address;
    private String email;
    private boolean isLoginStatus;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name ="Wallet_id")
    private Wallet wallet;
}
