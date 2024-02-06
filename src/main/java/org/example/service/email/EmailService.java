package org.example.service.email;

import org.example.dto.request.EmailRequest;
import org.example.dto.EmailResponse;

public interface EmailService {
 EmailResponse send(EmailRequest emailRequest);

}
