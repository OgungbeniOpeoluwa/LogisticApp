package org.example.service;

import org.example.dto.EmailRequest;
import org.example.dto.EmailResponse;

public interface EmailService {
 EmailResponse send(EmailRequest emailRequest);

}
