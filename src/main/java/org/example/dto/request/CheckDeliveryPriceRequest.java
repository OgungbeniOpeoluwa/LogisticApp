package org.example.dto.request;

import lombok.Data;

@Data
public class CheckDeliveryPriceRequest {
    private CheckPriceQuotationRequest pickUpAddress;
    private CheckPriceQuotationRequest deliver;

}
