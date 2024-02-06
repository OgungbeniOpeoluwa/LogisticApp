package org.example.service.vechicle;

import org.example.data.model.LogisticCompany;
import org.example.data.model.Vechicle;
import org.example.dto.request.RegisterVehicleRequest;

public interface VechicleService {
    Vechicle registerVechicle(LogisticCompany logisticCompany, RegisterVehicleRequest vechicleRequest);
}
