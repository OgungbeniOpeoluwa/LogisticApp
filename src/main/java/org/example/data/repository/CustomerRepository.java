package org.example.data.repository;

import org.example.data.model.Customers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customers,Long> {
    Customers findByEmail(String email);
}
