package org.example.service.logistic;

import org.example.data.model.LogisticCompany;
import org.example.data.model.Vechicle;
import org.example.data.repository.CompanyRepository;
import org.example.dto.request.CompanyLoginRequest;
import org.example.dto.request.LogisticRegisterRequest;
import org.example.dto.request.RegisterVehicleRequest;
import org.example.exception.*;
import org.example.service.vechicle.VechicleService;
import org.example.util.Mapper;
import org.example.util.Verification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LogisticServiceImpl implements LogisticsService{
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    VechicleService vechicleService;


    @Override
    public void register(LogisticRegisterRequest registerRequest) {
        if(userExist(registerRequest.getEmail()).isPresent())throw new UserExistException("User Exist");
        if(companyName(registerRequest.getCompanyName()))throw new UserExistException("Company Name already exist");
        if(!Verification.verifyEmail(registerRequest.getEmail()))throw new InvalidEmailException("Invalid email format");
        if(!Verification.verifyPassword(registerRequest.getPassword()))throw new InvalidPhoneNumberException("Invalid phone number");
        if(!Verification.verifyPhoneNumber(registerRequest.getPhoneNumber()))throw new InvalidPhoneNumberException("Wrong phone Number");
        LogisticCompany company = Mapper.mapCompany(registerRequest);
        companyRepository.save(company);

    }

    @Override
    public void login(CompanyLoginRequest companyLoginRequest) {
        if(!companyName(companyLoginRequest.getCompanyName()))throw new InvalidLoginDetail("Invalid login details");
        LogisticCompany logisticCompany = companyRepository.findByCompanyName(companyLoginRequest.getCompanyName());
        if(!checkPassword(companyLoginRequest.getCompanyName(), companyLoginRequest.getPassword()))throw new InvalidLoginDetail("Invalid login details");
        logisticCompany.setLoginStatus(true);
        companyRepository.save(logisticCompany);

    }

    @Override
    public void registerVechicle(RegisterVehicleRequest vechicleRequest) {
        LogisticCompany logisticCompany = companyRepository.findByCompanyName(vechicleRequest.getCompanyName());
        if(logisticCompany == null)throw new UserExistException("Company doesn't exist");
        if(!logisticCompany.isLoginStatus())throw new AppLockedException("Kindly login");
        Vechicle vechicle = vechicleService.registerVechicle(logisticCompany,vechicleRequest);
        logisticCompany.getVechicles().add(vechicle);
        companyRepository.save(logisticCompany);
    }

    @Override
    public List<Vechicle> findAllVechicle(String companyName) {
        LogisticCompany logisticCompany = companyRepository.findByCompanyName(companyName);
        if(logisticCompany == null)throw new UserExistException("Company doesn't exist");
        if(!logisticCompany.isLoginStatus())throw new AppLockedException("Kindly login");
        return logisticCompany.getVechicles() ;
    }

    private boolean companyName(String companyName) {
        LogisticCompany companyNames = companyRepository.findByCompanyName(companyName);
        return companyNames != null;
    }

    private Optional <LogisticCompany> userExist(String email) {
        return companyRepository.findByEmail(email);
    }

    private boolean checkPassword(String name,String password){
        LogisticCompany logisticCompany= companyRepository.findByCompanyName(name);
        BCryptPasswordEncoder decodePassword = new BCryptPasswordEncoder();
        return decodePassword.matches(password,logisticCompany.getPassword());
    }

}
