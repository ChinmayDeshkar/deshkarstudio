package com.crm.deshkarStudio.services.impl;

import com.crm.deshkarStudio.model.CustomerPurchases;
import com.crm.deshkarStudio.model.PurchaseHistory;
import com.crm.deshkarStudio.services.PurchaseHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@Service
public class PurchaseHistoryServiceImpl implements PurchaseHistoryService {
    @Override
    public PurchaseHistory addToPurchaseHistory(CustomerPurchases purchases) {

        PurchaseHistory history = new PurchaseHistory();
        history.setPurchaseId(purchases.getPurchaseId());
        history.setCustomer(purchases.getCustomer());
        history.setPrice(purchases.getPrice());
        history.setPaymentMethod(purchases.getPaymentMethod());
        history.setPaymentStatus(purchases.getPaymentStatus());
        history.setOrderStatus(purchases.getOrderStatus());
        history.setAdvancePaid(purchases.getAdvancePaid());
        history.setBalance(purchases.getBalance());
        history.setUpdatedDate(LocalDateTime.now(ZoneId.of("Asia/Kolkata")));
        history.setUpdatedBy(purchases.getUpdatedBy());
        history.setRemarks(purchases.getRemarks());

        log.info("History to be inserted: " + history);
        return null;
    }
}
