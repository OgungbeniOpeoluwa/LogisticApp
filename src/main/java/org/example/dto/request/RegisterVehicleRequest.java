package org.example.dto.request;

import lombok.Data;

@Data
public class RegisterVehicleRequest {
    private String plateNumber;
    private String vehicleType;
    private String vehicleWeightCapacity;
    private String driverLicenceNumber;
    private String companyName;
}
