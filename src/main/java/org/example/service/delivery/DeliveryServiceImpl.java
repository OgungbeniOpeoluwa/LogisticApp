package org.example.service.delivery;

import jakarta.transaction.Transactional;
import org.example.data.model.Delivery;
import org.example.data.model.DeliveryStatus;
import org.example.data.model.LogisticCompany;
import org.example.data.repository.DeliveryRepository;
import org.example.dto.request.BookDeliveryRequest;
import org.example.dto.request.AcceptBookingRequest;
import org.example.dto.response.BookingResponse;
import org.example.exception.DeliveryException;
import org.example.exception.InputException;
import org.example.service.admin.AdministratorService;
import org.example.util.GenerateBookingId;
import org.example.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DeliveryServiceImpl implements  DeliveryService{
    @Autowired
    DeliveryRepository deliveryRepository;
    @Autowired
    AdministratorService administratorService;
    private final int appPercent = 20;
    @Override
    public BookingResponse bookDelivery(BookDeliveryRequest bookDeliveryRequest, double price) {
        String generateId ="Bk-"+GenerateBookingId.generateId();
        double prices = (appPercent * price)/100;
        double companyPrice = price - prices;
        Delivery delivery = Mapper.mapDelivery(bookDeliveryRequest);
        delivery.setCompanyAmount(companyPrice);
        delivery.setDeliveryPrice(price);
        delivery.setBookingId(generateId);
        deliveryRepository.save(delivery);
        BookingResponse bookingResponse = new BookingResponse();
        bookingResponse.setBookingId(generateId);
        bookingResponse.setDelivery(delivery);
        return bookingResponse;
    }

    @Override
    public Delivery updateDeliveryRequest(AcceptBookingRequest acceptBookingRequest, LogisticCompany logisticCompany) {
        Delivery delivery = deliveryRepository.findByBookingId(acceptBookingRequest.getBookingId());
        if(delivery == null)throw new DeliveryException("Booking id doesn't exist");
        if(!delivery.getDeliveryStatus().equals(DeliveryStatus.PENDING))throw new DeliveryException("Delivery has been "+
                delivery.getDeliveryStatus());

        if(acceptBookingRequest.getResponse().equalsIgnoreCase("Accepted")){
            delivery.setDeliveryStatus(DeliveryStatus.ACCEPTED);
            delivery.setCompany(logisticCompany);
            deliveryRepository.save(delivery);
            return delivery;
        }

        if (acceptBookingRequest.getResponse().equalsIgnoreCase("Rejected")) {
            delivery.setDeliveryStatus(DeliveryStatus.REJECTED);
            deliveryRepository.save(delivery);
            return delivery;
        }
        throw new DeliveryException("Invalid input");

    }

    @Override
    public Delivery cancelDelivery(String bookingId, String customerEmail) {
        Delivery delivery = deliveryRepository.findByBookingId(bookingId);
        if(delivery == null)throw new DeliveryException("Booking id doesn't exist");
        if(!delivery.getCustomerEmail().equalsIgnoreCase(customerEmail))throw new DeliveryException("Wrong details format");
        if(delivery.getDeliveryStatus() == DeliveryStatus.DELIVERED)throw new DeliveryException("Delivery has been completed. it cant be canceled");
        if(delivery.getDeliveryStatus() == DeliveryStatus.REJECTED)throw new DeliveryException("Delivery was rejected cant be canceled");
        delivery.setDeliveryStatus(DeliveryStatus.CANCELLED);
        deliveryRepository.save(delivery);
             return delivery;


    }

    @Override
    public Delivery findLogisticDeliveryById(LogisticCompany logisticCompany, String bookingId) {
        List <Delivery> deliveries = findAllLogisticDelivery(logisticCompany);
        for(Delivery delivery:deliveries){
            if(delivery.getBookingId().equals(bookingId)) {
                return delivery;
            }
        }
        return null;
    }

//    @Override
//    public void deleteDelivery(LogisticCompany logisticCompany, String bookingId) {
//        Delivery delivery = findAllLogisticDelivery(logisticCompany,bookingId);
//        if(delivery == null)throw new DeliveryException("delivery with booking id doesn't exist");
//        deliveryRepository.delete(delivery);
//    }

    @Override
    public Delivery searchByDeliveryId(String bookingId, String Email) {
        Delivery delivery = deliveryRepository.findByBookingId(bookingId);
        if(delivery != null) {
            if (delivery.getCustomerEmail().equals(Email)) return delivery;
            else if(delivery.getLogisticCompany().equalsIgnoreCase(Email))return delivery;
        }
        return null;
    }
    @Override
    public List<Delivery> findAllLogisticDelivery(LogisticCompany logisticCompany){
        List <Delivery> deliveries = new ArrayList<>();
        for(Delivery delivery:deliveryRepository.findAll()){
            System.out.println(delivery);
            if(delivery.getCompany().equals(logisticCompany))deliveries.add(delivery);
        }
        return deliveries;

    }

    private List<LogisticCompany> alllogisticCompany(){
        List<LogisticCompany> allLogisticCompany = new ArrayList<>();
        for(Delivery delivery:deliveryRepository.findAll()){
           allLogisticCompany.add(delivery.getCompany());
        }
        return allLogisticCompany;
    }
    private LogisticCompany searchForLogisticCompany(String companyName){
        List<LogisticCompany> allLogisticCompany = alllogisticCompany();
        for(LogisticCompany logisticCompany: allLogisticCompany){
            if(logisticCompany.getCompanyName().equalsIgnoreCase(companyName))return logisticCompany;
        }
        return null;    }

    @Override
    public List<Delivery> searchDeliveryStatus(String companyName, String deliveryStatus) {
        List<Delivery> deliveryByStatus = new ArrayList<>();
       LogisticCompany logisticCompany = searchForLogisticCompany(companyName);
       if(logisticCompany == null)throw new DeliveryException("Company doesn't exist");
       List<Delivery> deliveries = findAllLogisticDelivery(logisticCompany);
       if(deliveries.isEmpty())throw new DeliveryException("No deliveries available for this company");
       for(Delivery delivery: deliveries){
           if(delivery.getDeliveryStatus().name().equalsIgnoreCase(deliveryStatus)){
               deliveryByStatus.add(delivery);
           }
       }

        return  deliveryByStatus;
    }
    @Override
    public List<Delivery> findAllCustomerDelivery(String email){
        List<Delivery> allDeliveries = new ArrayList<>();
        for(Delivery delivery:deliveryRepository.findAll()){
            if(delivery.getCustomerEmail().equals(email))allDeliveries.add(delivery);
        }
        return allDeliveries;
    }

    @Override
    public String getOrderStatus(String bookingId) {
        List<Delivery> allDelivery = deliveryRepository.findAll();
        for(Delivery delivery:allDelivery){
            if(delivery.getBookingId().equals(bookingId))return delivery.getDeliveryStatus().name();
        }
        return null;
    }

    @Override
    public Delivery updateDelivery(String update, LogisticCompany logisticCompany, String bookingId) {
        Delivery delivery = searchByDeliveryId(bookingId,logisticCompany.getCompanyName());
        if(delivery == null)throw new DeliveryException("Delivery order doesn't exist");
        if(delivery.getCompany() == null)throw new DeliveryException("No company has been assign to the delivery");
        if(DeliveryStatus.IN_TRANSIT.name().equalsIgnoreCase(update)){
            delivery.setDeliveryStatus(DeliveryStatus.IN_TRANSIT);
            deliveryRepository.save(delivery);
            return delivery;
        }
        else if (DeliveryStatus.DELIVERED.name().equalsIgnoreCase(update)){
            delivery.setDeliveryStatus(DeliveryStatus.DELIVERED);
            deliveryRepository.save(delivery);
            return delivery;
        }
        else throw new InputException("Invalid input message");


    }

    @Override
    public List<Delivery> searchDeliveryByStatus(String deliveryStatus, String email) {
        List<Delivery> allDeliveries = findAllCustomerDelivery(email);
        List<Delivery> deliveries = new ArrayList<>();
        for(Delivery delivery:allDeliveries){
            if(delivery.getDeliveryStatus().name().equalsIgnoreCase(deliveryStatus))deliveries.add(delivery);
        }
        return deliveries;
    }



}
