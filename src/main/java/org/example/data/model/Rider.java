package org.example.data.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Rider extends  User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String plateNumber;
    private String nameOfVechicle;
    private String driverLicence;
    private String colourOfVechicle;
    @OneToMany
    private List<Delivery> deliveries;

}
