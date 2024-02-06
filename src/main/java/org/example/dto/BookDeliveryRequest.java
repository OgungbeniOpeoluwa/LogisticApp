package org.example.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookDeliveryRequest {
    @NotNull
    private String receiverName;
    @NotNull
    private String receiverStreet;
    @NotNull
    private String receiverState;
    @NotNull
    private String receiverCity;
    @NotNull
    private String receiverPhoneNumber;

    private String receiverEmail;

    private String pickUpState;
    private String pickUpCity;
    private String pickUpStreet;
    private String pickUpName;

    private String pickUpPhoneNumber;

    @NotNull
    private String typeOfPackage;

    @NotNull
    private String customerEmail;
    @NotNull
    private int packageWeight;
    @NotNull
    private String typeOfVechicle;

    private String logisticCompanyEmail;
}
