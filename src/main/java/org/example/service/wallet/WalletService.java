package org.example.service.wallet;

import org.example.dto.WalletRegisterRequest;
import org.example.dto.request.FundWalletRequest;
import org.example.dto.request.LoginRequest;
import org.example.dto.response.CheckBalanceResponse;
import org.example.dto.response.FundWalletResponse;
import org.example.dto.response.LoginResponse;
import org.example.dto.response.WalletRegisterResponse;

public interface WalletService {
    WalletRegisterResponse createWallet(WalletRegisterRequest walletRegisterRequest);

    LoginResponse login(LoginRequest loginRequest);

    FundWalletResponse fundWallet(FundWalletRequest request, String url);

    CheckBalanceResponse checkBalance(String url);
}
