package org.example.service.vechicle;

import jakarta.transaction.Transactional;
import org.example.data.model.Delivery;
import org.example.data.model.LogisticCompany;
import org.example.data.model.TypeOfVehicle;
import org.example.data.model.Vechicle;
import org.example.data.repository.VechicleRepository;
import org.example.dto.request.RegisterVehicleRequest;
import org.example.dto.request.SetDayAvailabiltyRequest;
import org.example.exception.DeliveryException;
import org.example.exception.VechicleException;
import org.example.service.delivery.DeliveryService;
import org.example.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class VechicleServiceImpl implements VechicleService {
    @Autowired
    VechicleRepository vechicleRepository;

    @Override
    public void registerVechicle(LogisticCompany logisticCompany, RegisterVehicleRequest vechicleRequest) {
        List <Vechicle> userVechicle = findAllVechilcleBelongingToUser(logisticCompany);
            for (Vechicle vechicle : userVechicle) {
                if (vechicle.getVechicleType().equals(vechicleRequest.getVehicleType()))
                    throw new VechicleException("Vehicle already exist");
            }
            if(!loopThroughTheEnumValueOfVechicleType(vechicleRequest.getVehicleType()))throw new VechicleException("vehicle type is not available");
       Vechicle vehicles = Mapper.mapVechicle(vechicleRequest);
       vehicles.setCompany(logisticCompany);
        vechicleRepository.save(vehicles);

    }
    @Override
    public List<Vechicle> findAllVechilcleBelongingToUser(LogisticCompany logisticCompany){
        List<Vechicle> allLogisticVechilce = new ArrayList<>();
        for(Vechicle vechicle:vechicleRepository.findAll()){
            if(vechicle.getCompany().getId().equals(logisticCompany.getId()))allLogisticVechilce.add(vechicle);
        }
        return allLogisticVechilce;
    }

    @Override
    public void deleteVechicle(String vechicleType, LogisticCompany logisticCompany) {
        List<Vechicle> allVechicle = findAllVechilcleBelongingToUser(logisticCompany);
        for(Vechicle vechicle:allVechicle){
            if(vechicle.getVechicleType().equalsIgnoreCase(vechicleType)){
                vechicleRepository.delete(vechicle);
            }
        }

    }

    @Override
    public Vechicle setVechicleLimitPerDay(SetDayAvailabiltyRequest availabiltyRequest,LogisticCompany logisticCompany) {
        List<Vechicle> userVechicle = findAllVechilcleBelongingToUser(logisticCompany);
        if(userVechicle.isEmpty())throw new VechicleException("No vehicle register under company name");
        if(checkIfThereIsPendingDelivery(availabiltyRequest.getVechicleType(),logisticCompany))throw new DeliveryException("Kindly deliver pending deliveries");
        for (Vechicle vechicle : userVechicle) {
            if (vechicle.getVechicleType().equalsIgnoreCase(availabiltyRequest.getVechicleType()))
                vechicle.setLimitPerDay(availabiltyRequest.getNumber());
            vechicleRepository.save(vechicle);
            return vechicle;

        }
        throw new VechicleException("vehicle name doesn't exist");
    }
    private boolean checkIfThereIsPendingDelivery(String nameOfVechicleType,LogisticCompany logisticCompany){
       List<Vechicle> allVechicle = findAllVechilcleBelongingToUser(logisticCompany);
       for(Vechicle vechicle: allVechicle){
           if(vechicle.getLimitPerDay() > 0)return true;
       }
       return false;
    }

    @Override
    public void updateLimit(LogisticCompany logisticCompany, String nameOfVechicle) {
        List<Vechicle> vechicles = findAllVechilcleBelongingToUser(logisticCompany);
        if(vechicles.isEmpty())throw new VechicleException("No vehicle register under company name");
        for (Vechicle vechicle : vechicles) {
            if (vechicle.getVechicleType().equalsIgnoreCase(nameOfVechicle)) {
                int count = vechicle.getLimitPerDay() - 1;
                vechicle.setLimitPerDay(count);
                vechicleRepository.save(vechicle);
            }
        }

    }

    @Override
    public Vechicle addToLimit(LogisticCompany logisticCompany, String nameOfVechicle) {
        List<Vechicle> vechicles = findAllVechilcleBelongingToUser(logisticCompany);
        if(vechicles.isEmpty())throw new VechicleException("No vehicle register under company name");
        for (Vechicle vechicle : vechicles) {
            if (vechicle.getVechicleType().equalsIgnoreCase(nameOfVechicle)) {
                int count = vechicle.getLimitPerDay() + 1;
                vechicle.setLimitPerDay(count);
                vechicleRepository.save(vechicle);
                return vechicle;
            }
        }
                return null;

    }



    private boolean loopThroughTheEnumValueOfVechicleType(String vehicleType){
        for(TypeOfVehicle vehicle: TypeOfVehicle.values()){
            if(vehicle.name().equalsIgnoreCase(vehicleType))return true;
        }
        return false;
    }
}
