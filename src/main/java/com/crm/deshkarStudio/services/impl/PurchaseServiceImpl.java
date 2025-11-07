package com.crm.deshkarStudio.services.impl;

import com.crm.deshkarStudio.dto.PurchaseDTO;
import com.crm.deshkarStudio.model.Customer;
import com.crm.deshkarStudio.model.CustomerPurchases;
import com.crm.deshkarStudio.repo.CustomerPurchasesRepo;
import com.crm.deshkarStudio.repo.CustomerRepo;
import com.crm.deshkarStudio.services.PurchaseService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {

    private final CustomerRepo customerRepo;
    private final CustomerPurchasesRepo purchaseRepo;

    @Override
    public ResponseEntity<?> addPurchase(Map<String, Object> payload) {
        String name = (String) payload.get("customerName");
        String phone = (String) payload.get("phoneNumber");
        String email = (String) payload.get("email");
        String address = (String) payload.get("address");

        double price = Double.parseDouble(payload.get("price").toString());
        String paymentMethod = (String) payload.get("paymentMethod");
        String paymentStatus = (String) payload.get("paymentStatus");
        double balance = Double.parseDouble(payload.get("balance").toString());
        String remarks = (String) payload.get("remarks");

        // ✅ 1. Check if customer already exists
        Customer customer = customerRepo.findByPhoneNumber(phone)
                .orElseGet(() -> {
                    Customer newCustomer = new Customer();
                    newCustomer.setCustomerName(name);
                    newCustomer.setEmail(email);
                    newCustomer.setPhoneNumber(phone);
                    newCustomer.setAddress(address);
                    newCustomer.setCreatedDate(LocalDate.now());
                    return customerRepo.save(newCustomer);
                });

        // ✅ 2. Add new purchase for the found/created customer
        CustomerPurchases purchase = new CustomerPurchases();
        purchase.setCustomer(customer);
        purchase.setPrice(price);
        purchase.setPaymentMethod(paymentMethod);
        purchase.setPaymentStatus(paymentStatus);
        purchase.setBalance(balance);
        purchase.setRemarks(remarks);
        purchase.setCreatedDate(LocalDateTime.now());
        purchaseRepo.save(purchase);

        return ResponseEntity.ok(Map.of("message", "Purchase added successfully", "customerId", customer.getId()));
    }

//    /** ✅ Purchases made today */
//    public List<CustomerPurchases> getTodayPurchases() {
//        LocalDate today = LocalDate.now();
//        LocalDateTime startOfDay = today.atStartOfDay();
//        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();
//        return purchaseRepo.findByCreatedDateBetween(startOfDay, endOfDay);
//    }
//
//    /** ✅ Purchases made this month */
//    public List<CustomerPurchases> getMonthlyPurchases() {
//        LocalDate today = LocalDate.now();
//        LocalDate firstDay = today.withDayOfMonth(1);
//        LocalDate lastDay = today.withDayOfMonth(today.lengthOfMonth());
//        log.info("Last Date: " + lastDay.plusDays(1).atStartOfDay());
//        log.info("firstDay Date: " + firstDay.atStartOfDay());
//        return purchaseRepo.findByCreatedDateBetween(firstDay.atStartOfDay(), lastDay.plusDays(1).atStartOfDay());
//    }
//
//    /** ✅ Purchases in a specific date range */
//    public List<CustomerPurchases> getPurchasesByDateRange(LocalDate startDate, LocalDate endDate) {
//        return purchaseRepo.findByCreatedDateBetween(startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay());
//    }


    private PurchaseDTO mapToDTO(CustomerPurchases p) {
        return new PurchaseDTO(
                p.getPurchaseId(),
                p.getCustomer().getCustomerName(),
                p.getCustomer().getPhoneNumber(),
                p.getPrice(),
                p.getPaymentMethod(),
                p.getPaymentStatus(),
                p.getBalance(),
                p.getCreatedDate(),
                p.getRemarks()
        );
    }

    public List<PurchaseDTO> getTodayPurchases() {

        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        log.info("Fetching purchases between {} and {}", startOfDay, endOfDay);

        return purchaseRepo.findByCreatedDateBetween(startOfDay, endOfDay)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());


//        log.info("Checking for datetime - " + LocalDateTime.now());
//        return purchaseRepo.findByCreatedDate(LocalDateTime.now())
//                .stream()
//                .map(this::mapToDTO)
//                .collect(Collectors.toList());
    }

    public List<PurchaseDTO> getPurchasesThisMonth() {
        return purchaseRepo.findPurchasesThisMonth()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<PurchaseDTO> getPurchasesByRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Fetching purchases from {} to {}", startDate, endDate);

        return purchaseRepo.findByCreatedDateBetween(startDate, endDate)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
}
