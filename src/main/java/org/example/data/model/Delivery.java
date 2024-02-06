package org.example.data.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String recieverName;
    private String recieverEmail;

    private String recieverAddress;
    private String recieverPhoneNumber;

    private String pickUpAddress;
    private String pickUpUserName;
    private String pickUpPhoneNumber;
    private String typeOfPackage;

    private String nameOfVechicle;

    private String customerEmail;

    private String logisticComapny;

    private int packageWeight;
    private String bookingId;


}
