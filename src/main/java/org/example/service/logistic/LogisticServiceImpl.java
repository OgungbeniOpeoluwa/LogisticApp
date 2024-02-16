package org.example.service.logistic;

import jakarta.transaction.Transactional;
import org.example.data.model.*;
import org.example.data.repository.CompanyRepository;
import org.example.dto.request.*;
import org.example.exception.*;
import org.example.service.admin.AdministratorService;
import org.example.service.delivery.DeliveryService;
import org.example.service.transaction.TransactionService;
import org.example.service.vechicle.VechicleService;
import org.example.util.Mapper;
import org.example.util.Verification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LogisticServiceImpl implements LogisticsService{
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    VechicleService vechicleService;
    @Autowired
    DeliveryService deliveryService;
    @Autowired
    AdministratorService administratorService;
    @Autowired
    TransactionService transactionService;


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
        LogisticCompany logisticCompany = companyRepository.findByCompanyName(companyLoginRequest.getCompanyName());
        if(!companyName(companyLoginRequest.getCompanyName()))throw new InvalidLoginDetail("Invalid login details");
        if(!checkPassword(companyLoginRequest.getCompanyName(), companyLoginRequest.getPassword()))throw new InvalidLoginDetail("Invalid login details");
        logisticCompany.setLoginStatus(true);
        companyRepository.save(logisticCompany);

    }

    @Override
    public void registerVechicle(RegisterVehicleRequest vechicleRequest) {
        LogisticCompany logisticCompany = companyRepository.findByCompanyName(vechicleRequest.getCompanyName());
        if(logisticCompany == null)throw new UserExistException("Company doesn't exist");
        if(!logisticCompany.isLoginStatus())throw new AppLockedException("Kindly login");
        vechicleService.registerVechicle(logisticCompany,vechicleRequest);
        companyRepository.save(logisticCompany);
    }

    @Override
    public List<Vechicle> findAllVechicle(String companyName) {
        LogisticCompany logisticCompany = companyRepository.findByCompanyName(companyName);
        if(logisticCompany == null)throw new UserExistException("Company doesn't exist");
        if(!logisticCompany.isLoginStatus())throw new AppLockedException("Kindly login");
        return vechicleService.findAllVechilcleBelongingToUser(logisticCompany);
    }

    @Override
    public List<LogisticCompany> findAvailableLogisticCompany() {
        List<LogisticCompany> logisticCompanies = new ArrayList<>();
        for(LogisticCompany logisticCompany :companyRepository.findAll()){
            if(logisticCompany.isLoginStatus())logisticCompanies.add(logisticCompany);
        }
        return logisticCompanies;
    }

    @Override
    public void responseToBookingRequest( AcceptBookingRequest acceptBookingRequest) {
        LogisticCompany logisticCompany = companyRepository.findByCompanyName(acceptBookingRequest.getCompanyName());
        if(!companyName(acceptBookingRequest.getCompanyName()))throw new UserExistException(acceptBookingRequest.getCompanyName()+" company doesn't exist");
        if(!logisticCompany.isLoginStatus())throw new AppLockedException("Kindly login");
        Delivery delivery = deliveryService.updateDeliveryRequest(acceptBookingRequest,logisticCompany);
        if(acceptBookingRequest.getResponse().equalsIgnoreCase("Accepted")){
            vechicleService.updateLimit(logisticCompany,delivery.getNameOfVechicle());
            companyRepository.save(logisticCompany);
        }
        administratorService.UpdateDeliveryEmail(delivery,acceptBookingRequest.getResponse());


    }

    @Override
    public void setDayAvailability(SetDayAvailabiltyRequest availabilityRequest) {
        LogisticCompany logisticCompany = companyRepository.findByCompanyName(availabilityRequest.getCompanyName());
        if(!companyName(availabilityRequest.getCompanyName()))throw new UserExistException(availabilityRequest.getCompanyName()+" company doesn't exist");
        if(!logisticCompany.isLoginStatus())throw new AppLockedException("Kindly login");
        vechicleService.setVechicleLimitPerDay(availabilityRequest,logisticCompany);
        companyRepository.save(logisticCompany);

    }

    @Override
    public LogisticCompany checkLogisticCompany(String logisticCompanyName, String typeOfVechicle) {
        LogisticCompany logisticCompany = companyRepository.findByCompanyName(logisticCompanyName);
        if(logisticCompany == null)throw new LogisticException("Logistic company doesn't exist");
        List<Vechicle> allVechicle = vechicleService.findAllVechilcleBelongingToUser(logisticCompany);
        for(Vechicle vechicle: allVechicle) {
            if (vechicle.getVechicleType().equalsIgnoreCase(typeOfVechicle) && vechicle.getLimitPerDay() == 0) {
                throw new LogisticException("Logistic company is currently not available");
            }
            else if(vechicle.getVechicleType().equalsIgnoreCase(typeOfVechicle) && vechicle.getLimitPerDay() > 0){
                return logisticCompany;
            }
        }
        throw new LogisticException("Vehicle type doesn't exist");
    }

    @Override
    public LogisticCompany resetLogistic(String companyName, String bookingId, String nameOfVechicle) {
        LogisticCompany logisticCompany = companyRepository.findByCompanyName(companyName);
        if(logisticCompany == null) throw new LogisticException("Logistic company doesn't exist");
        Vechicle vechicle = vechicleService.addToLimit(logisticCompany,nameOfVechicle);
        if(vechicle == null)throw new VechicleException("Vechicle type doesn't exist");
        companyRepository.save(logisticCompany);
        return logisticCompany;
    }

    @Override
    public List<Delivery> findAllDeliveries(String companyName) {
        LogisticCompany logisticCompany = companyRepository.findByCompanyName(companyName);
        if(logisticCompany == null)throw new LogisticException("Logistic company doesn't exist");
        if(!logisticCompany.isLoginStatus())throw new AppLockedException("Kindly login");
        return deliveryService.findAllLogisticDelivery(logisticCompany);
    }

    @Override
    public void cancelDelivery(CancelBookingRequest cancelBookingRequest) {
        LogisticCompany logisticCompany = companyRepository.findByCompanyName(cancelBookingRequest.getCompanyName());
        if(logisticCompany == null)throw new LogisticException("Logistic company doesn't exist");
        if(!logisticCompany.isLoginStatus())throw new AppLockedException("Kindly login");
        Delivery delivery = deliveryService.cancelDelivery(cancelBookingRequest.getBookingId(), cancelBookingRequest.getCustomerEmail() );
        resetLogistic(cancelBookingRequest.getCompanyName(), cancelBookingRequest.getBookingId(), delivery.getNameOfVechicle());
        administratorService.cancelBookingEmail(delivery.getCustomerEmail(),delivery.getBookingId(),cancelBookingRequest.getReasonForCancellation(),
                delivery.getCustomerEmail(), delivery.getDeliveryPrice());
    }

    @Override
    public List<Delivery> searchBydeliveryStatus(SearchByDeliveryStatusRequest searchByDeliveryStatusRequest) {
        LogisticCompany logisticCompany = companyRepository.findByCompanyName(searchByDeliveryStatusRequest.getCompanyName());
        if(logisticCompany == null)throw new LogisticException("Logistic company doesn't exist");
        if(!logisticCompany.isLoginStatus())throw new AppLockedException("Kindly login");
        return deliveryService.searchDeliveryStatus(searchByDeliveryStatusRequest.getCompanyName(),searchByDeliveryStatusRequest.getDeliveryStatus());

    }

    @Override
    public void updateDeliveryStatus(UpdateDeliveryStatusRequest updateDeliveryStatusRequest) {
        LogisticCompany logisticCompany = companyRepository.findByCompanyName(updateDeliveryStatusRequest.getCompanyName());
        if(logisticCompany == null)throw new LogisticException("Logistic company doesn't exist");
        if(!logisticCompany.isLoginStatus())throw new AppLockedException("Kindly login");
        Delivery delivery = deliveryService.updateDelivery(updateDeliveryStatusRequest.getUpdate(),logisticCompany,updateDeliveryStatusRequest.getBookingId());
        if(delivery.getDeliveryStatus().equals(DeliveryStatus.DELIVERED)){
            transactionService.addTransaction(delivery.getCustomerEmail(),delivery.getCompanyAmount(),delivery.getBookingId(),logisticCompany);
        }

    }

    @Override
    public void deleteVechicle(DeleteVechicleRequest deleteVechicleRequest) {
        LogisticCompany logisticCompany = companyRepository.findByCompanyName(deleteVechicleRequest.getCompanyName());
        if(logisticCompany == null)throw new LogisticException("Logistic company doesn't exist");
        if(!logisticCompany.isLoginStatus())throw new AppLockedException("Kindly login");
        vechicleService.deleteVechicle(deleteVechicleRequest.getVechicleType(),logisticCompany);

    }

    @Override
    public List<Transaction> getTransactions(GetTransactionRequest getTransactionRequest) {
        LogisticCompany logisticCompany = companyRepository.findByCompanyName(getTransactionRequest.getCompanyName());
        if(logisticCompany == null)throw new LogisticException("Logistic company doesn't exist");
        if(!logisticCompany.isLoginStatus())throw new AppLockedException("Kindly login");
        List<Transaction> transactions = transactionService.getAllTransaction(logisticCompany);
        if(transactions.isEmpty())throw new TransactionsException("No Transaction Available for "+ getTransactionRequest.getCompanyName());
        if(getTransactionRequest.getEmail() == null){
            administratorService.sendTransaction(logisticCompany.getEmail(),transactions,getTransactionRequest.getCompanyName());
            return transactions;
        }
        administratorService.sendTransaction(getTransactionRequest.getEmail(),transactions,getTransactionRequest.getCompanyName());
        return transactions;
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
