package org.example.data.repository;

import org.example.data.model.Customers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customers,Long> {
    Optional<Customers> findByEmail(String email);
}
