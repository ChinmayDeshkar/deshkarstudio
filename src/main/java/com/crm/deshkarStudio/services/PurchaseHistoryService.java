package com.crm.deshkarStudio.services;

import com.crm.deshkarStudio.model.CustomerPurchases;
import com.crm.deshkarStudio.model.PurchaseHistory;

public interface PurchaseHistoryService {

    PurchaseHistory addToPurchaseHistory(CustomerPurchases purchases);
}
