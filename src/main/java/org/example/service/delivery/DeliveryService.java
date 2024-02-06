package org.example.service.delivery;

import org.example.dto.BookDeliveryRequest;

public interface DeliveryService {
    String bookDelivery(BookDeliveryRequest bookDeliveryRequest);
}
