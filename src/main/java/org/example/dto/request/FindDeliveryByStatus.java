package org.example.dto.request;

import lombok.Data;

@Data
public class FindDeliveryByStatus {
    private String email;
    private String deliveryStatus;
}
