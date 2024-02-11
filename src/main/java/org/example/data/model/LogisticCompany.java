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
    private String  socialMediaLink;
    private String password;
    private String address;
    private boolean isLoginStatus;
    private String email;
    private String cacNumber;

}
