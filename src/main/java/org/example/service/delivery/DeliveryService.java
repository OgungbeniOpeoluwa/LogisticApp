package org.example.service.delivery;

import org.example.data.model.Delivery;
import org.example.data.model.LogisticCompany;
import org.example.dto.BookDeliveryRequest;
import org.example.dto.request.AcceptBookingRequest;
import org.example.dto.response.BookingResponse;

import java.util.List;

public interface DeliveryService {
    BookingResponse bookDelivery(BookDeliveryRequest bookDeliveryRequest, double price);

    Delivery updateDeliveryRequest(AcceptBookingRequest acceptBookingRequest, LogisticCompany logisticCompany);

    Delivery cancelDelivery(String bookingId, String customerEmail);

    Delivery findLogisticDeliveryById(LogisticCompany logisticCompany, String bookingId);

  //  void deleteDelivery(LogisticCompany logisticCompany, String bookingId);

    Delivery searchByDeliveryId(String bookingId, String customerEmail);

    List<Delivery> findAllLogisticDelivery(LogisticCompany logisticCompany);

    List<Delivery> searchDeliveryStatus(String companyName, String deliveryStatus);

    String getOrderStatus(String bookingId, String email);

    Delivery updateDelivery(String update, LogisticCompany logisticCompany, String bookingId);

    List<Delivery> searchDeliveryByStatus(String deliveryStatus, String email);

    List<Delivery> findAllCustomerDelivery(String email);
}
