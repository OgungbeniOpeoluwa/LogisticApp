package org.example.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.example.data.model.Delivery;

@Data
public class BookingResponse {
    private String bookingId;
    @JsonIgnore
    private Delivery delivery;
    private double deliveryPrice;
}
