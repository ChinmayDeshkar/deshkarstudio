package com.crm.deshkarStudio.contoller;

import com.crm.deshkarStudio.dto.PurchaseDTO;
import com.crm.deshkarStudio.dto.PurchaseDetailsDTO;
import com.crm.deshkarStudio.dto.RevenueDTO;
import com.crm.deshkarStudio.dto.TaskDTO;
import com.crm.deshkarStudio.model.CustomerPurchases;
import com.crm.deshkarStudio.repo.CustomerPurchasesRepo;
import com.crm.deshkarStudio.services.PurchaseService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/purchases")
@AllArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final CustomerPurchasesRepo purchaseRepo;

    @PostMapping("/add")
    public ResponseEntity<?> addPurchase(@RequestBody CustomerPurchases purchase) {
        log.info("Request Body: "+ purchase.toString());
        return purchaseService.addPurchase(purchase);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseDetailsDTO> getPurchaseById(@PathVariable long id) {
        log.info("Getting purchases by id: " + id);
        PurchaseDetailsDTO dto = purchaseService.getPurchaseById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/cust-id/{id}")
    public ResponseEntity<List<CustomerPurchases>> getPurchaseByCustId(@PathVariable long id) {
        log.info("Getting purchases by customer id: " + id);
        List<CustomerPurchases> dto = purchaseService.getPurchaseByCustId(id);
        return ResponseEntity.ok(dto);
    }


    @GetMapping("/today")
    public ResponseEntity<List<CustomerPurchases>> getTodayPurchases() {
        return ResponseEntity.ok(purchaseService.getTodayPurchases());
    }

    @GetMapping("/month")
    public ResponseEntity<List<CustomerPurchases>> getThisMonthPurchases() {
        return ResponseEntity.ok(purchaseService.getPurchasesThisMonth());
    }

    @GetMapping("/range")
    public ResponseEntity<List<PurchaseDTO>> getPurchasesByRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);
        return ResponseEntity.ok(purchaseService.getPurchasesByRange(start, end));
    }

    @GetMapping("/revenue-past-seven")
    public List<RevenueDTO> getRevenuePerDay() {
        return purchaseService.getRevenuePerDay();
    }

    @GetMapping("/revenue-month")
    public List<RevenueDTO> getRevenuePerMonth() {
        return purchaseService.getRevenuePerMonth();
    }

    @GetMapping("/revenue-year")
    public List<RevenueDTO> getRevenuePerYear() {
        return purchaseService.getRevenuePerYear();
    }

    @GetMapping("/revenue-range")
    public List<RevenueDTO> getRevenueByRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        return purchaseService.getRevenueByRange(start, end);
    }

    @GetMapping("/revenue-payment-method")
    public List<RevenueDTO> getTransactionCountByPaymentMethod() {
        return purchaseService.getTransactionCountByPaymentMethod();
    }

    @GetMapping("/pending-tasks")
    public ResponseEntity<?> getPendingTasks() {
        List<TaskDTO> pendingTasks = purchaseService.getPendingTasks();
        return ResponseEntity.ok(pendingTasks);
    }

    @GetMapping("/recent-tasks")
    public List<CustomerPurchases> getRecentTasks() {
        return purchaseService.getRecentTasks();
    }

    @PutMapping("/update-order-status/{purchaseId}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable long purchaseId, @RequestBody String updatedOrderStatus){
        purchaseService.updateOrderStatus(purchaseId, updatedOrderStatus);
        return ResponseEntity.ok(Map.of("Message", "Order status updated"));
    }

    @PutMapping("/update-payment-status/{purchaseId}")
    public ResponseEntity<?> updatePaymentStatus(@PathVariable long purchaseId, @RequestBody String updatedPaymentStatus){
        log.info(purchaseId + ": " + updatedPaymentStatus);
        purchaseService.updatePaymentStatus(purchaseId, updatedPaymentStatus);
        return ResponseEntity.ok(Map.of("Message", "Payment Status updated"));
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<CustomerPurchases> updatePurchase(@PathVariable long id, @RequestBody CustomerPurchases purchase){
        log.info("Coming coming");
        return ResponseEntity.ok(purchaseService.updatePurchase(id, purchase));
    }

}
