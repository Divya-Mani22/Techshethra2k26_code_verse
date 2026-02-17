package com.example.hackthon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.hackthon.entity.Registration;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    // Intha line-a add pannunga
    boolean existsByTransactionId(String transactionId);
}