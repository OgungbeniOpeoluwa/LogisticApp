package org.example.beanConfig;

import jakarta.validation.Validator;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Configuration
@Getter
public class ConfigBean {
    @Value("${WALLET.REGISTRATION.URL}")
    private String walletRegistrationUrl;
    @Value("${WALLET_BALANCE}")
    private String getBalance;
    @Value("${WALLET_LOGIN}")
    private String walletLogin;
    @Value("${FUND_WALLET}")
    private String fundWallet;

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate(){return  new RestTemplate();}
}
