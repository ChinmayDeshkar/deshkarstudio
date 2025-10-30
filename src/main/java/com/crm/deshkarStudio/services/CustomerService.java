package com.crm.deshkarStudio.services;

import com.crm.deshkarStudio.model.CustomerPurchases;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface CustomerService {
    public boolean customerExists(String phoneNumber) ;
    public CustomerPurchases addPurchase(Map<String, Object> payload) ;
    public ResponseEntity<?> customerDetails(String phoneNumber);
}
