package org.example.data.repository;

import org.example.data.model.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdministratorRepository extends JpaRepository<Administrator,Long> {
    Administrator findByUsername(String admin);
}
