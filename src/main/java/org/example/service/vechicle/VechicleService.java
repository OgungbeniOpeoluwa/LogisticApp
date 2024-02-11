package org.example.service.vechicle;

import org.example.data.model.LogisticCompany;
import org.example.data.model.Vechicle;
import org.example.dto.request.RegisterVehicleRequest;
import org.example.dto.request.SetDayAvailabiltyRequest;

import java.util.List;

public interface VechicleService {
    void registerVechicle(LogisticCompany logisticCompany, RegisterVehicleRequest vechicleRequest);

    Vechicle setVechicleLimitPerDay(SetDayAvailabiltyRequest availabiltyRequest,LogisticCompany logisticCompany);

    void updateLimit(LogisticCompany logisticCompany, String nameOfVechicle);

    Vechicle addToLimit(LogisticCompany logisticCompany,String nameOfVechicle);

    List<Vechicle> findAllVechilcleBelongingToUser(LogisticCompany logisticCompany);

    void deleteVechicle(String vechicleType, LogisticCompany logisticCompany);
}
