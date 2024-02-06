package org.example.service.logistic;

import org.example.data.model.Vechicle;
import org.example.dto.request.CompanyLoginRequest;
import org.example.dto.request.LogisticRegisterRequest;
import org.example.dto.request.RegisterVehicleRequest;

import java.util.List;

public interface LogisticsService {
    void register(LogisticRegisterRequest registerRequest);

    void login(CompanyLoginRequest companyLoginRequest);

    void registerVechicle(RegisterVehicleRequest vechicleRequest);

    List<Vechicle> findAllVechicle(String companyName);
}
