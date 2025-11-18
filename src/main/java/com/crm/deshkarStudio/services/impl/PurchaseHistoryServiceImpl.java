package com.crm.deshkarStudio.services.impl;

import com.crm.deshkarStudio.model.CustomerPurchases;
import com.crm.deshkarStudio.model.PurchaseHistory;
import com.crm.deshkarStudio.repo.PurchaseHistoryrepo;
import com.crm.deshkarStudio.services.PurchaseHistoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@Service
@AllArgsConstructor
public class PurchaseHistoryServiceImpl implements PurchaseHistoryService {

    private final PurchaseHistoryrepo purchaseHistoryrepo;

    @Override
    public PurchaseHistory addToPurchaseHistory(CustomerPurchases purchases, String note) {
        PurchaseHistory history = new PurchaseHistory();
        history.setPurchaseId(purchases.getPurchaseId());
        history.setUpdatedBy(purchases.getUpdatedBy());
        history.setNote(note);

        purchaseHistoryrepo.save(history);
        return null;
    }
}
