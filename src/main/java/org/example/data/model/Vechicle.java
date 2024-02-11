package org.example.data.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Vechicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String vechicleType;
    private int limitPerDay = 0;
    @ManyToOne(cascade = {CascadeType.ALL},fetch = FetchType.LAZY)
    private LogisticCompany company;
}
