package org.example.service.admin;

import org.example.data.model.Administrator;
import org.example.data.repository.AdministratorRepository;
import org.example.dto.request.EmailRequest;
import org.example.service.email.EmailService;
import org.example.service.wallet.WalletService;
import org.example.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdministratorServiceImpl implements AdministratorService {
    @Autowired
    AdministratorRepository administratorRepository;
    @Autowired
    EmailService emailService;
    @Autowired
    WalletService walletService;

    @Override
    public void depositConfirmationEmail(String email, double amount) {
        String description = String.format("""
                Payment request from %s
                Amount %s""",email,amount);
        String title = "Deposit to wallet";
        Administrator admin = administratorRepository.findByUsername("Admin");

        EmailRequest emailRequest = Mapper.emailRequest(admin.getEmail(),title,description);
        emailService.send(emailRequest);

    }

    @Override
    public void updateWallet(String email) {
         walletService.updateWallet(email);
         String title = "Update On Deposit";
         String description ="Hello " +email+ " Your wallet has succesfully be credited,you can now check your balance";
         EmailRequest emailRequest = Mapper.emailRequest(email,title,description);
         emailService.send(emailRequest);


    }
}
