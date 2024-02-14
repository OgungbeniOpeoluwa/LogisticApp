package org.example.data.repository;

import org.example.data.model.Account;
import org.example.data.model.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,Long> {
    Account findByAdministrator(Administrator administrator);
}
