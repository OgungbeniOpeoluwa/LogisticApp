package org.example.service;

import jakarta.transaction.Transactional;
import org.example.dto.WalletRegisterRequest;
import org.example.dto.request.FundWalletRequest;
import org.example.dto.request.LoginRequest;
import org.example.dto.response.CheckBalanceResponse;
import org.example.dto.response.FundWalletResponse;
import org.example.dto.response.LoginResponse;
import org.example.dto.response.WalletRegisterResponse;
import org.example.service.wallet.WalletService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class WalletServiceTest {
    @Autowired
    private WalletService walletService;

    @Test
    public void testCreateWallet(){
        WalletRegisterRequest walletRegisterRequest = new WalletRegisterRequest();
        walletRegisterRequest.setWallet_pin("0000");
        walletRegisterRequest.setUsername("Mia");
        walletRegisterRequest.setEmail("opeAgne@gmail.com");
        walletRegisterRequest.setPassword("ope12344");

        WalletRegisterResponse walletRegisterResponse = walletService.createWallet(walletRegisterRequest);


        assertThat(walletRegisterResponse.getEmail()).isEqualTo("opeAgne@gmail.com");
    }
    @Test
    public void loginToWallet(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("ope12344");
        loginRequest.setUsername("Mia");

        LoginResponse response = walletService.login(loginRequest);
        System.out.println(response.getAccess());
        assertThat(response).isNotNull();
  }

  @Test
    public  void fundWallet(){
        FundWalletRequest request = new FundWalletRequest();
        request.setAmount(200);
        String url ="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNzE1MDExMzM3LC" +
                        "JpYXQiOjE3MTQ5MjQ5MzcsImp0aSI6IjEwMjdlNzQ2MGJhYTQzNTU4OWM4YmRjOTMwMGJiMDBlIiwid" +
                        "XNlcl9pZCI6MTF9.RNa_qszN5DEyDVQFVR2GiWt2tWHdTM2J66VCEzRkf9k";

        FundWalletResponse walletResponse = walletService.fundWallet(request,url);
        assertThat(walletResponse).isNotNull();


    }
    @Test
    public void  getWalletBalance(){
        String url = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNzE1MDExMzM3LC" +
                "JpYXQiOjE3MTQ5MjQ5MzcsImp0aSI6IjEwMjdlNzQ2MGJhYTQzNTU4OWM4YmRjOTMwMGJiMDBlIiwid" +
                "XNlcl9pZCI6MTF9.RNa_qszN5DEyDVQFVR2GiWt2tWHdTM2J66VCEzRkf9k";

        CheckBalanceResponse balanceResponse =  walletService.checkBalance(url);

        assertThat(balanceResponse).isNotNull();
    }
}
