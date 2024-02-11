package org.example.dto.request;

import lombok.Data;

@Data
public class TrackOrderRequest {
    private String bookingId;
    private String email;
}
