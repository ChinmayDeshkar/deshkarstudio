package com.crm.deshkarStudio.repo;

import com.crm.deshkarStudio.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvoiceRepo extends JpaRepository<Invoice, Long> {
    Optional<Invoice> findByPurchaseId(Long purchaseId);
}
