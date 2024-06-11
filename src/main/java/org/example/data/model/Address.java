package org.example.data.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Address{
    @Id
    private Long id;
    private String state;
    private String city;
    private String street;
    @OneToOne
    private Customers customers;
}
