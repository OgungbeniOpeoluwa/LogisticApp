package org.example.dto.request;

import lombok.Data;

@Data
public class FindABookedDeliveryRequest {
    private String customerEmail;
    private String bookingId;
}
