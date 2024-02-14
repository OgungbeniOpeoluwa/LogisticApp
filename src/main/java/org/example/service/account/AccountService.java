package org.example.service.account;

import org.example.data.model.Account;
import org.example.data.model.Administrator;
import org.example.dto.request.AccountRequest;

public interface AccountService {
    void setAccount(AccountRequest accountRequest, Administrator administrator);

    Account getAccount(Administrator administrator);
}
