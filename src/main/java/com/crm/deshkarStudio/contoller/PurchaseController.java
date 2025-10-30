package com.crm.deshkarStudio.contoller;

import com.crm.deshkarStudio.dto.PurchaseDTO;
import com.crm.deshkarStudio.model.CustomerPurchases;
import com.crm.deshkarStudio.services.PurchaseService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

//    /** ✅ Purchases made today */
//    @GetMapping("/today")
//    public ResponseEntity<List<CustomerPurchases>> getTodayPurchases() {
//
//        List<CustomerPurchases> purchases = purchaseService.getTodayPurchases();
//        return ResponseEntity.ok(purchases);
//    }
//
//    /** ✅ Purchases for current month */
//    @GetMapping("/month")
//    public ResponseEntity<List<CustomerPurchases>> getMonthlyPurchases() {
//        List<CustomerPurchases> purchases = purchaseService.getMonthlyPurchases();
//        return ResponseEntity.ok(purchases);
//    }
//
//    /** ✅ Purchases between selected dates (inclusive) */
//    @GetMapping("/range")
//    public ResponseEntity<List<CustomerPurchases>> getPurchasesByDateRange(
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
//        List<CustomerPurchases> purchases = purchaseService.getPurchasesByDateRange(startDate, endDate);
//        return ResponseEntity.ok(purchases);
//    }


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
        return ResponseEntity.ok(purchaseService.getPurchasesByRange(startDate, endDate));
    }

}
