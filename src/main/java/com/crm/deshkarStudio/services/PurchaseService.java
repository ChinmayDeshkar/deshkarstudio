package com.crm.deshkarStudio.services;

import com.crm.deshkarStudio.dto.PurchaseDTO;
import com.crm.deshkarStudio.dto.PurchaseDetailsDTO;
import com.crm.deshkarStudio.dto.RevenueDTO;
import com.crm.deshkarStudio.dto.TaskDTO;
import com.crm.deshkarStudio.model.Customer;
import com.crm.deshkarStudio.model.CustomerPurchases;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface PurchaseService {

    ResponseEntity<?> addPurchase(CustomerPurchases purchase);

    PurchaseDetailsDTO getPurchaseById(long id);

    List<TaskDTO> getPurchaseByCustId(long id);

    List<TaskDTO> getPurchaseByPhoneNumber(String phoneNumber);

    List<TaskDTO> getTodayPurchases() ;

    List<TaskDTO> getPurchasesThisMonth() ;

    List<TaskDTO> getPurchasesByRange(LocalDateTime start, LocalDateTime end) ;

    List<TaskDTO> getPendingTasks();

    List<CustomerPurchases> getRecentTasks();

    CustomerPurchases updatePurchase(long purchaseId, CustomerPurchases purchase);
}
