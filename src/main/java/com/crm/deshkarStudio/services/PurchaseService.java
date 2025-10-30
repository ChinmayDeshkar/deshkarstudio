package com.crm.deshkarStudio.services;

import com.crm.deshkarStudio.dto.PurchaseDTO;
import com.crm.deshkarStudio.model.CustomerPurchases;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface PurchaseService {

    public ResponseEntity<?> addPurchase(Map<String, Object> payload);
    /** ✅ Purchases made today */
//    public List<CustomerPurchases> getTodayPurchases();
//
//    /** ✅ Purchases made this month */
//    public List<CustomerPurchases> getMonthlyPurchases();
//
//    /** ✅ Purchases in a specific date range */
//    public List<CustomerPurchases> getPurchasesByDateRange(LocalDate startDate, LocalDate endDate);

    public List<PurchaseDTO> getTodayPurchases() ;

    public List<PurchaseDTO> getPurchasesThisMonth() ;

    public List<PurchaseDTO> getPurchasesByRange(LocalDate start, LocalDate end) ;
}
