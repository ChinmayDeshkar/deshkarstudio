package com.crm.deshkarStudio.services;

import com.crm.deshkarStudio.model.CustomerPurchases;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface CustomerService {
    boolean customerExists(String phoneNumber) ;
    CustomerPurchases addPurchase(Map<String, Object> payload) ;
    ResponseEntity<?> customerDetails(String phoneNumber);

}
