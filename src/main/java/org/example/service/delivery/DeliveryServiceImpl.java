package org.example.service.delivery;

import org.example.data.model.Delivery;
import org.example.data.repository.DeliveryRepository;
import org.example.dto.BookDeliveryRequest;
import org.example.util.GenerateBookingId;
import org.example.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveryServiceImpl implements  DeliveryService{
    @Autowired
    DeliveryRepository deliveryRepository;
    @Override
    public String bookDelivery(BookDeliveryRequest bookDeliveryRequest) {
        String generateId ="Bk-"+GenerateBookingId.generateId();
        Delivery delivery = Mapper.mapDelivery(bookDeliveryRequest);
        delivery.setBookingId(generateId);
        deliveryRepository.save(delivery);
        return generateId;
    }


}
