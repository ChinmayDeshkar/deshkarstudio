package com.crm.deshkarStudio.services;

import com.crm.deshkarStudio.dto.PurchaseDTO;
import com.crm.deshkarStudio.dto.RevenueDTO;
import com.crm.deshkarStudio.model.CustomerPurchases;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface PurchaseService {

    ResponseEntity<?> addPurchase(Map<String, Object> payload);

    List<PurchaseDTO> getTodayPurchases() ;

    List<PurchaseDTO> getPurchasesThisMonth() ;

    List<PurchaseDTO> getPurchasesByRange(LocalDateTime start, LocalDateTime end) ;

    public List<RevenueDTO> getRevenuePerDay();

    public List<RevenueDTO> getRevenuePerMonth();

    public List<RevenueDTO> getRevenuePerYear();

    public List<RevenueDTO> getRevenueByRange(LocalDateTime start, LocalDateTime end);

    public List<RevenueDTO> getTransactionCountByPaymentMethod();
}
