package org.example.dto;

import lombok.Data;

@Data
public class CheckDeliveryPriceRequest {
    private CheckPriceQuotationRequest pickUpAddress;
    private CheckPriceQuotationRequest deliver;

}
