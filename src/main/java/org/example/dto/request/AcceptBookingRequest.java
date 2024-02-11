package org.example.dto.request;

import lombok.Data;

@Data
public class AcceptBookingRequest {
    private String companyName;
    private String bookingId;
    private String response;

}
