package org.example.dto.request;

import lombok.Data;

@Data
public class CheckPriceQuotationRequest {
    private String typeOfVehicle;
    private int weightOfPackage;

    private String pickUpCity;
    private String pickUpStreet;
    private String pickUpState;

    private String deliveryCity;
    private String deliveryStreet;
    private String deliveryState;

}
