package com.crm.deshkarStudio.repo;

import com.crm.deshkarStudio.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, String> {
    Optional<Customer> findByCustomerName(String customerName);
    Optional<Customer> findByPhoneNumber(String phoneNumber);
}
