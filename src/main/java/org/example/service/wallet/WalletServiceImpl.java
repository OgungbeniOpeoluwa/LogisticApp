package org.example.service.wallet;

import lombok.AllArgsConstructor;
import org.example.beanConfig.ConfigBean;
import org.example.dto.WalletRegisterRequest;
import org.example.dto.request.FundWalletRequest;
import org.example.dto.request.LoginRequest;
import org.example.dto.response.CheckBalanceResponse;
import org.example.dto.response.FundWalletResponse;
import org.example.dto.response.LoginResponse;
import org.example.dto.response.WalletRegisterResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Service
@AllArgsConstructor
public class WalletServiceImpl implements WalletService{
    private ConfigBean configBean;
    private RestTemplate restTemplate;
    @Override
    public WalletRegisterResponse createWallet(WalletRegisterRequest wallet) {
        HttpEntity<?> v = new HttpEntity<>(wallet);
        ResponseEntity<WalletRegisterResponse> responses = restTemplate.postForEntity(configBean.getWalletRegistrationUrl(),v,WalletRegisterResponse.class);
        return responses.getBody();
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        HttpEntity<?> v = new HttpEntity<>(loginRequest);
        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(configBean.getWalletLogin(),v,LoginResponse.class);
         return response.getBody();
    }

    @Override
    public FundWalletResponse fundWallet(FundWalletRequest request, String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization","Bearer "+url);
        HttpEntity<?> v = new HttpEntity<>(request,headers);
        ResponseEntity<FundWalletResponse> response = restTemplate.postForEntity(configBean.getFundWallet(),v,FundWalletResponse.class);
        return response.getBody();
    }

    @Override
    public CheckBalanceResponse checkBalance(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization","Bearer "+url);
        HttpEntity<?> v = new HttpEntity<>(headers);
        ResponseEntity<CheckBalanceResponse> response = restTemplate.exchange(configBean.getGetBalance(), HttpMethod.GET,v,CheckBalanceResponse.class);
        return response.getBody();
    }
}
