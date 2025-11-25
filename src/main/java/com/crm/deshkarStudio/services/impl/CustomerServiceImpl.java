package com.crm.deshkarStudio.services.impl;

import com.crm.deshkarStudio.model.Customer;
import com.crm.deshkarStudio.model.CustomerPurchases;
import com.crm.deshkarStudio.repo.CustomerPurchasesRepo;
import com.crm.deshkarStudio.repo.CustomerRepo;
import com.crm.deshkarStudio.services.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepo customerRepo;
    private final CustomerPurchasesRepo purchaseRepo;

    public boolean customerExists(String phoneNumber) {
        return customerRepo.findByPhoneNumber(phoneNumber).isPresent();
    }

    public CustomerPurchases addPurchase(Map<String, Object> payload) {
        String phone = payload.get("phoneNumber").toString();

        Customer customer = customerRepo.findByPhoneNumber(phone)
                .orElseGet(() -> {
                    Customer newCust = new Customer();
                    newCust.setCustomerName((String) payload.get("customerName"));
                    newCust.setEmail((String) payload.get("email"));
                    newCust.setPhoneNumber(phone);
                    newCust.setAddress((String) payload.get("address"));
                    return customerRepo.save(newCust);
                });

        CustomerPurchases purchase = new CustomerPurchases();
        purchase.setCustomer(customer);
        purchase.setPrice(Double.parseDouble(payload.get("price").toString()));
        purchase.setPaymentMethod((String) payload.get("paymentMethod"));
        purchase.setPaymentStatus((String) payload.get("paymentStatus"));
        purchase.setBalance(Double.parseDouble(payload.get("balance").toString()));
        purchase.setRemarks((String) payload.get("remarks"));

        return purchaseRepo.save(purchase);
    }

    @Override
    public ResponseEntity<?> customerDetails(String phoneNumber) {
        log.debug("Customer exists, getting details...");
        Customer customer = customerRepo.findByPhoneNumber(phoneNumber).orElseThrow(() -> new RuntimeException("Customer doesn't exists"));
        return ResponseEntity.ok(Map.of(
                "exists", true,
                "id", customer.getId(),
                "customerName", customer.getCustomerName(),
                "email", customer.getEmail(),
                "phoneNumber", customer.getPhoneNumber(),
                "address", customer.getAddress(),
                "purchaseCount", customer.getPurchaseCount()
        ));
    }
}
