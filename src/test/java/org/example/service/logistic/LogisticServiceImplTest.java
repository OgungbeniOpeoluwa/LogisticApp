package org.example.service.logistic;

import org.example.data.model.Vechicle;
import org.example.data.repository.CompanyRepository;
import org.example.data.repository.VechicleRepository;
import org.example.dto.request.CompanyLoginRequest;
import org.example.dto.request.LogisticRegisterRequest;
import org.example.dto.request.RegisterVehicleRequest;
import org.example.exception.InvalidLoginDetail;
import org.example.service.vechicle.VechicleService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@TestPropertySource(locations = "/test.properties")
class LogisticServiceImplTest {
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    LogisticsService logisticsService;
    @Autowired
    VechicleRepository vechicleRepository;
    @AfterEach
    public void doThisAfterAll(){
       companyRepository.deleteAll();
       vechicleRepository.deleteAll();

    }
    @Test
    public void testThatWhenCompanyRegisterCountIsOne(){
        LogisticRegisterRequest registerRequest = new LogisticRegisterRequest();
        registerRequest.setCompanyName("Vision five Company");
        registerRequest.setPassword("Opemip@1");
        registerRequest.setAddress("10 gbagada estate");
        registerRequest.setEmail("ogunbeniopemipo1@gmail.com");
        registerRequest.setCacNumber("Ba123h565");
        registerRequest.setPhoneNumber("07066221006");
        logisticsService.register(registerRequest);
        assertEquals(1,companyRepository.count());

    }
    @Test
    public void testThatWhenUserLoginWithWrongPasswordThrowsException(){
        LogisticRegisterRequest registerRequest = new LogisticRegisterRequest();
        registerRequest.setCompanyName("Vision five Company");
        registerRequest.setPassword("Opemip@1");
        registerRequest.setAddress("10 gbagada estate");
        registerRequest.setEmail("ogunbeniopemipo1@gmail.com");
        registerRequest.setCacNumber("Ba123h565");
        registerRequest.setPhoneNumber("07066221006");
        logisticsService.register(registerRequest);

        CompanyLoginRequest companyLoginRequest = new CompanyLoginRequest();
        companyLoginRequest.setCompanyName(registerRequest.getCompanyName());
        companyLoginRequest.setPassword("12444");
        assertThrows(InvalidLoginDetail.class,()->logisticsService.login(companyLoginRequest));
    }
    @Test
    public void testThatWhenCompanyLoginWithWrongCompanyNameThrowsException(){
        LogisticRegisterRequest registerRequest = new LogisticRegisterRequest();
        registerRequest.setCompanyName("Vision five Company");
        registerRequest.setPassword("Opemip@1");
        registerRequest.setAddress("10 gbagada estate");
        registerRequest.setEmail("ogunbeniopemipo1@gmail.com");
        registerRequest.setCacNumber("Ba123h565");
        registerRequest.setPhoneNumber("07066221006");
        logisticsService.register(registerRequest);

        CompanyLoginRequest companyLoginRequest = new CompanyLoginRequest();
        companyLoginRequest.setCompanyName("Shola company");
        companyLoginRequest.setPassword(registerRequest.getPassword());
        assertThrows(InvalidLoginDetail.class,()->logisticsService.login(companyLoginRequest));

    }
    @Test
    public void testThatWhenCompanyCanSetUp2VechiclesAndIfIndAllVechicleOfCompanyCountIsTwo(){
        LogisticRegisterRequest registerRequest = new LogisticRegisterRequest();
        registerRequest.setCompanyName("Vision five Company");
        registerRequest.setPassword("Opemip@1");
        registerRequest.setAddress("10 gbagada estate");
        registerRequest.setEmail("ogunbeniopemipo1@gmail.com");
        registerRequest.setCacNumber("Ba123h565");
        registerRequest.setPhoneNumber("07066221006");
        logisticsService.register(registerRequest);

        CompanyLoginRequest companyLoginRequest = new CompanyLoginRequest();
        companyLoginRequest.setCompanyName(registerRequest.getCompanyName());
        companyLoginRequest.setPassword(registerRequest.getPassword());
        logisticsService.login(companyLoginRequest);

        RegisterVehicleRequest vechicleRequest = new RegisterVehicleRequest();
        vechicleRequest.setVehicleType("Bike");
        vechicleRequest.setPlateNumber("AA2345");
        vechicleRequest.setDriverLicenceNumber("B123552GD");
        vechicleRequest.setVehicleWeightCapacity("50");
        vechicleRequest.setCompanyName(registerRequest.getCompanyName());
        logisticsService.registerVechicle(vechicleRequest);

        RegisterVehicleRequest vechicleRequest2 = new RegisterVehicleRequest();
        vechicleRequest2.setVehicleType("Car");
        vechicleRequest2.setPlateNumber("AA2346");
        vechicleRequest2.setDriverLicenceNumber("B123552GD");
        vechicleRequest2.setCompanyName(registerRequest.getCompanyName());
        vechicleRequest2.setVehicleWeightCapacity("50");
        logisticsService.registerVechicle(vechicleRequest2);

        List<Vechicle> allVechicle = logisticsService.findAllVechicle(registerRequest.getCompanyName());
        assertEquals(2,allVechicle.size());

    }


}