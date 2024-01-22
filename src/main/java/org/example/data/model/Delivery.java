package org.example.data.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String recieverName;
    private String recieverAddress;
    private String recieverPhoneNumber;
    private String pickUpAddress;
    private String pickUpUserName;
    private String pickUpPhoneNumber;
    private String typeOfPackage;
    private String nameOfVechicle;
    private String recieverEmail;
    private String customerEmail;


}
