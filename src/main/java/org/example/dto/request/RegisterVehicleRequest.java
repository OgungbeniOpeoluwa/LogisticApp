package org.example.dto.request;

import lombok.Data;

@Data
public class RegisterVehicleRequest {
    private String vehicleType;
    private String companyName;
}
