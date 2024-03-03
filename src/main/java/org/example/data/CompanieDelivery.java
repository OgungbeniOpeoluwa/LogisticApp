package org.example.data;

import lombok.Data;
import org.example.data.model.DeliveryStatus;

@Data
public class CompanieDelivery {
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

    private String logisticCompany;

    private double companyAmount;

    private double deliveryPrice;

    private int packageWeight;

    private String bookingId;

    private DeliveryStatus deliveryStatus;
}
