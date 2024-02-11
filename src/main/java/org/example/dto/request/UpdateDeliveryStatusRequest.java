package org.example.dto.request;

import lombok.Data;

@Data
public class UpdateDeliveryStatusRequest {
    private String companyName;
    private String  bookingId;
    private String update;
}
