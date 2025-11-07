package com.crm.deshkarStudio.contoller;

import com.crm.deshkarStudio.dto.PurchaseDTO;
import com.crm.deshkarStudio.dto.RevenueDTO;
import com.crm.deshkarStudio.model.CustomerPurchases;
import com.crm.deshkarStudio.services.PurchaseService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/purchases")
@AllArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping("/add")
    public ResponseEntity<?> addPurchase(@RequestBody Map<String, Object> body) {
        log.info("Request Body: "+ body);
        return purchaseService.addPurchase(body);
    }

    @GetMapping("/today")
    public ResponseEntity<List<PurchaseDTO>> getTodayPurchases() {
        return ResponseEntity.ok(purchaseService.getTodayPurchases());
    }

    @GetMapping("/month")
    public ResponseEntity<List<PurchaseDTO>> getThisMonthPurchases() {
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
}
