package com.crm.deshkarStudio.services.impl;

import com.crm.deshkarStudio.dto.PurchaseDTO;
import com.crm.deshkarStudio.dto.RevenueDTO;
import com.crm.deshkarStudio.dto.TaskDTO;
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
import java.util.ArrayList;
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
    public ResponseEntity<?> addPurchase(CustomerPurchases purchase) {
        Customer payloadCustomer = purchase.getCustomer();
        // ✅ 1. Check if customer already exists
        Customer customer = customerRepo.findByPhoneNumber(payloadCustomer.getPhoneNumber())
                .orElseGet(() -> {
                    Customer newCustomer = new Customer();
                    newCustomer.setCustomerName(payloadCustomer.getCustomerName());
                    newCustomer.setEmail(payloadCustomer.getEmail());
                    newCustomer.setPhoneNumber(payloadCustomer.getPhoneNumber());
                    newCustomer.setAddress(payloadCustomer.getAddress());
                    newCustomer.setCreatedDate(LocalDate.now());
                    return customerRepo.save(newCustomer);
                });
        purchase.setCustomer(customer);
        // ✅ 2. Add new purchase for the found/created customer

        if(purchase.getAdvancePaid() < purchase.getPrice()){
            double balance = purchase.getPrice() - purchase.getAdvancePaid();
            purchase.setBalance(balance);
            if(balance > 0)
                purchase.setPaymentStatus("PENDING");

        }
        log.info("Purchase to be added : " + purchase);
        purchase.setOrderStatus("CREATED");

        purchaseRepo.save(purchase);

        return ResponseEntity.ok(Map.of("message", "Purchase added successfully", "customerId", customer.getId()));
    }

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

    public List<CustomerPurchases> getTodayPurchases() {

        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        log.info("Fetching purchases between {} and {}", startOfDay, endOfDay);

        return purchaseRepo.findByCreatedDateBetween(startOfDay, endOfDay);
    }

    public List<CustomerPurchases> getPurchasesThisMonth() {
        return purchaseRepo.findPurchasesThisMonth();
    }

    public List<PurchaseDTO> getPurchasesByRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Fetching purchases from {} to {}", startDate, endDate);

        return purchaseRepo.findByCreatedDateBetween(startDate, endDate)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RevenueDTO> getRevenuePerDay() {
        return mapResult(purchaseRepo.getRevenuePerDay(), "day");
    }

    @Override
    public List<RevenueDTO> getRevenuePerMonth() {
        return mapResult(purchaseRepo.getRevenuePerMonth(), "month");
    }

    @Override
    public List<RevenueDTO> getRevenuePerYear() {
        return mapResult(purchaseRepo.getRevenuePerYear(), "year");
    }

    @Override
    public List<RevenueDTO> getRevenueByRange(LocalDateTime start, LocalDateTime end) {
        return mapResult(purchaseRepo.getRevenueBetween(start, end), "day");
    }

    @Override
    public List<RevenueDTO> getTransactionCountByPaymentMethod() {
        List<RevenueDTO> list = new ArrayList<>();
        for (Object[] obj : purchaseRepo.getTransactionCountByPaymentMethod()) {
            String method = (String) obj[0];
            long count = ((Number) obj[1]).longValue();
            list.add(new RevenueDTO(method, 0, 0));
        }
        return list;
    }

    private List<RevenueDTO> mapResult(List<Object[]> results, String type) {
        List<RevenueDTO> list = new ArrayList<>();
        for (Object[] obj : results) {
            String label;

            if (type.equals("month")) {
                // For monthly query: obj[0] = month, obj[1] = year
                label = obj[1] + "-" + obj[0];
            } else if (type.equals("year")) {
                label = obj[0].toString();
            } else {
                label = obj[0].toString();
            }

            // ✅ Correct index usage
            double income = obj[1] != null ? ((Number) obj[1]).doubleValue() : 0;
            double balance = obj[2] != null ? ((Number) obj[2]).doubleValue() : 0;
            long count = obj[3] != null ? ((Number) obj[3]).longValue() : 0;

            list.add(new RevenueDTO(label, income, balance));
        }
        return list;
    }

    public List<TaskDTO> getPendingTasks() {
        List<CustomerPurchases> purchases = purchaseRepo.findPendingTasks();
        List<TaskDTO> tasks = new ArrayList<>(List.of());
        for(CustomerPurchases purchase: purchases){
            TaskDTO task = new TaskDTO();
            task.setPurchaseId(purchase.getPurchaseId());
            task.setCustomerName(purchase.getCustomer().getCustomerName());
            task.setPhoneNumber(purchase.getCustomer().getPhoneNumber());
            task.setPrice(purchase.getPrice());
            task.setBalance(purchase.getBalance());
            task.setPaymentStatus(purchase.getPaymentStatus());
            task.setOrderStatus(purchase.getOrderStatus());
            task.setRemark(purchase.getRemarks());
            task.setDte_created(purchase.getCreatedDate());
            tasks.add(task);
        }
        return tasks;
    }

    public List<CustomerPurchases> getRecentTasks() {
            List<String> statuses = List.of("COMPLETED", "DELIVERED", "CANCELLED");
        LocalDateTime fromDate = LocalDateTime.now().minusDays(5);

        return purchaseRepo.findRecentCompletedOrders(statuses, fromDate);
    }

    @Override
    public CustomerPurchases updateOrderStatus(long purchaseId, String updatedOrderStatus) {
        CustomerPurchases purchase = purchaseRepo.findById(purchaseId).orElseThrow(() -> new RuntimeException("No Purchase available"));
        purchase.setOrderStatus(updatedOrderStatus);
        return purchaseRepo.save(purchase);

    }

    @Override
    public CustomerPurchases updatePaymentStatus(long purchaseId, String updatedPaymentStatus) {
        CustomerPurchases purchase = purchaseRepo.findById(purchaseId).orElseThrow(() -> new RuntimeException("No Purchase available"));
        purchase.setPaymentStatus(updatedPaymentStatus);
        purchase.setBalance(0);

        return purchaseRepo.save(purchase);
    }

}
