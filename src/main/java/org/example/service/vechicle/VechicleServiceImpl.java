package org.example.service.vechicle;

import org.example.data.model.LogisticCompany;
import org.example.data.model.Vechicle;
import org.example.data.repository.VechicleRepository;
import org.example.dto.request.RegisterVehicleRequest;
import org.example.exception.VechicleException;
import org.example.service.vechicle.VechicleService;
import org.example.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VechicleServiceImpl implements VechicleService {
    @Autowired
    VechicleRepository vechicleRepository;
    @Override
    public Vechicle registerVechicle(LogisticCompany logisticCompany, RegisterVehicleRequest vechicleRequest) {
        Vechicle vechicle = vechicleRepository.findByPlateNumber(vechicleRequest.getPlateNumber());
        if(vechicle != null)throw new VechicleException("Vechicle already exist");
       Vechicle vechicles = Mapper.mapVechicle(vechicleRequest);
        vechicleRepository.save(vechicles);
        return vechicles;
    }
}
