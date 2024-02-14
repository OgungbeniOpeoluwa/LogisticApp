package org.example.service.account;

import org.example.data.model.Account;
import org.example.data.model.Administrator;
import org.example.data.repository.AccountRepository;
import org.example.dto.request.AccountRequest;
import org.example.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService{
    @Autowired
    AccountRepository accountRepository;

    @Override
    public void setAccount(AccountRequest accountRequest, Administrator admin) {
        Account account = Mapper.mapAccount(accountRequest);
        account.setAdministrator(admin);
        accountRepository.save(account);
    }

    @Override
    public Account getAccount(Administrator administrator) {
        return accountRepository.findByAdministrator(administrator);
    }
}
