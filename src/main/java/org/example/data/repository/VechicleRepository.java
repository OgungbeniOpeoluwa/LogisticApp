package org.example.data.repository;

import org.example.data.model.Vechicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VechicleRepository extends JpaRepository<Vechicle,Long> {
}
