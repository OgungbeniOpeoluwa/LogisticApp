package org.example.dto.request;

import lombok.Data;

@Data
public class SearchByDeliveryStatusRequest {
    private String companyName;
    private String deliveryStatus;
}
