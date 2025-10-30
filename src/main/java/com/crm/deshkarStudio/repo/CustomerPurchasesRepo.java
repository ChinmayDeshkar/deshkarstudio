package com.crm.deshkarStudio.repo;

import com.crm.deshkarStudio.model.CustomerPurchases;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CustomerPurchasesRepo extends JpaRepository<CustomerPurchases, String> {
    List<CustomerPurchases> findByCreatedDate(LocalDate date);

    @Query("SELECT p FROM CustomerPurchases p WHERE p.createdDate BETWEEN :start AND :end")
    List<CustomerPurchases> findByDateRange(LocalDate start, LocalDate end);

    @Query("SELECT p FROM CustomerPurchases p WHERE MONTH(p.createdDate) = MONTH(CURRENT_DATE) AND YEAR(p.createdDate) = YEAR(CURRENT_DATE)")
    List<CustomerPurchases> findPurchasesThisMonth();
}
