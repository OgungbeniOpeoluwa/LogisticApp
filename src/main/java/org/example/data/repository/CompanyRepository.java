package org.example.data.repository;

import org.example.data.model.LogisticCompany;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<LogisticCompany,Long> {
    Optional<LogisticCompany> findByEmail(String email);

    LogisticCompany findByCompanyName(String companyName);
}
