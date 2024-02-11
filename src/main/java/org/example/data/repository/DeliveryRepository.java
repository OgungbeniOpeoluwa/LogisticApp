package org.example.data.repository;

import org.example.data.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery,Long> {
    Delivery findByBookingId(String bookingId);

}
