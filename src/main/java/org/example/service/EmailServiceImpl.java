package org.example.service;

import org.example.dto.EmailRequest;
import org.example.dto.EmailResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService{
    @Autowired
    JavaMailSender javaMailSender;
    @Value("tifeagnes86@gmail.com")
    private String sender;
    @Override
    public EmailResponse send(EmailRequest emailRequest) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        try {
            mailMessage.setFrom(sender);
            mailMessage.setTo(emailRequest.getReciepent());
            mailMessage.setSubject(emailRequest.getTitle());
            mailMessage.setText(emailRequest.getBody());
            javaMailSender.send(mailMessage);
            EmailResponse emailResponse = new EmailResponse("Mail sent Successfully");
            return emailResponse;
        } catch (Exception ex) {
            return new EmailResponse("Error while sending Mail");
        }
    }
}
