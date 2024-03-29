package org.example.service.delivery;

import org.example.data.model.Delivery;
import org.example.data.model.LogisticCompany;
import org.example.dto.request.BookDeliveryRequest;
import org.example.dto.request.AcceptBookingRequest;
import org.example.dto.response.BookingResponse;
import org.example.data.CompanieDelivery;

import java.util.List;

public interface DeliveryService {
    BookingResponse bookDelivery(BookDeliveryRequest bookDeliveryRequest, double price);

    Delivery updateDeliveryRequest(AcceptBookingRequest acceptBookingRequest, LogisticCompany logisticCompany);

    Delivery cancelDelivery(String bookingId, String customerEmail);

    CompanieDelivery findLogisticDeliveryById(LogisticCompany logisticCompany, String bookingId);

  //  void deleteDelivery(LogisticCompany logisticCompany, String bookingId);

    Delivery searchByDeliveryId(String bookingId, String customerEmail);

    List<CompanieDelivery> findAllLogisticDelivery(LogisticCompany logisticCompany);

    List<CompanieDelivery> searchDeliveryStatus(String companyName, String deliveryStatus);

    String getOrderStatus(String bookingId);

    Delivery updateDelivery(String update, LogisticCompany logisticCompany, String bookingId);

    List<Delivery> searchDeliveryByStatus(String deliveryStatus, String email);

    List<Delivery> findAllCustomerDelivery(String email);
}
