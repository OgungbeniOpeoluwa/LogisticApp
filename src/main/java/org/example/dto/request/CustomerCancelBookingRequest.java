package org.example.dto.request;

import lombok.Data;

@Data
public class CustomerCancelBookingRequest {
    private  String bookingId;
    private String customerEmail;
    private  String reasonOnWhyBookingWasCancelled;
    private String companyName;
}

