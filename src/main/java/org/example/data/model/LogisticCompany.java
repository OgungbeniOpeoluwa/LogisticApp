package org.example.data.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class LogisticCompany{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Long id;
    private  String companyName;
    private String phoneNumber;
    private String password;
    private String address;
    private boolean isLoginStatus;
    private String email;
    private int limitPerDay;
    private String cacNumber;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_company_id",referencedColumnName = "company_id")
    private List<Transaction> transactions = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_company_id",referencedColumnName = "company_id")
    private List <Delivery> deliveries = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_company_id",referencedColumnName = "company_id")
    private List<Vechicle> vechicles = new ArrayList<>();
}
