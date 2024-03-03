package org.example.service.logistic;

import org.example.data.model.LogisticCompany;
import org.example.data.model.Transaction;
import org.example.data.model.Vechicle;
import org.example.dto.request.*;
import org.example.data.CompanieDelivery;

import java.util.List;

public interface LogisticsService {
    void register(LogisticRegisterRequest registerRequest);

    void login(CompanyLoginRequest companyLoginRequest);

    void registerVechicle(RegisterVehicleRequest vechicleRequest);

    List<Vechicle> findAllVechicle(String companyName);

    List<LogisticCompany> findAvailableLogisticCompany();

    void responseToBookingRequest(AcceptBookingRequest acceptBookingRequest);

    void setDayAvailability(SetDayAvailabiltyRequest availabiltyRequest);

    LogisticCompany checkLogisticCompany(String logisticCompanyEmail, String typeOfVechicle);
    LogisticCompany  resetLogistic(String companyName, String bookingId, String nameOfVechicle);

    List<CompanieDelivery> findAllDeliveries(String email);

    void cancelDelivery(CancelBookingRequest cancelBookingRequest);

    List<CompanieDelivery> searchBydeliveryStatus(SearchByDeliveryStatusRequest searchByDeliveryStatusRequest);

    void updateDeliveryStatus(UpdateDeliveryStatusRequest updateDeliveryStatusRequest);

    void deleteVechicle(DeleteVechicleRequest deleteVechicleRequest);

    List<Transaction> getTransactions(GetTransactionRequest getTransactionRequest);
}
