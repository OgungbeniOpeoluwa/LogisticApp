package org.example.dto.request;

import lombok.Data;

@Data
public class CancelBookingRequest {
    private String companyName;
    private String bookingId;
    private String customerEmail;
    private  String reasonForCancellation;
}
