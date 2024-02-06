package org.example.data.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Vechicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String plateNumber;
    private String vechicleType;
    private String vechicleWeightCapacity;
    private String driverLincenceNumber;
}
