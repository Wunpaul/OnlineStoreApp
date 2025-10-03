package com.coffencode.onlinestoreapplication.repositories;

import com.coffencode.onlinestoreapplication.entities.Customer;
import com.coffencode.onlinestoreapplication.dto.CustomerAuthDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);

    // ✅ Lean fetch for auth purposes
    @Query("SELECT new com.coffencode.onlinestoreapplication.dto.CustomerAuthDTO(c.id, c.name, c.email) " +
            "FROM Customer c WHERE c.email = :email")
    Optional<CustomerAuthDTO> findAuthDTOByEmail(String email);
}
