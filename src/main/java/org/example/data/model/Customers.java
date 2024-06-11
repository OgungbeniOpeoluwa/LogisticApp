package org.example.data.model;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Customers{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String phoneNumber;
    private String password;
    private String username;
    private String email;
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Roles> userRole = new HashSet<>();
    private String walletAuthorization;
}
