package com.crm.deshkarStudio.contoller;

import com.crm.deshkarStudio.dto.PurchaseDTO;
import com.crm.deshkarStudio.dto.PurchaseDetailsDTO;
import com.crm.deshkarStudio.dto.RevenueDTO;
import com.crm.deshkarStudio.dto.TaskDTO;
import com.crm.deshkarStudio.model.CustomerPurchases;
import com.crm.deshkarStudio.repo.CustomerPurchasesRepo;
import com.crm.deshkarStudio.services.NoteService;
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
    private final NoteService noteService;

    @PostMapping("/add")
    public ResponseEntity<?> addPurchase(@RequestBody CustomerPurchases purchase) {
        log.info(purchase.toString());
        return purchaseService.addPurchase(purchase);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseDetailsDTO> getPurchaseById(@PathVariable long id) {
        PurchaseDetailsDTO dto = purchaseService.getPurchaseById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/cust-id/{id}")
    public ResponseEntity<List<PurchaseDetailsDTO>> getPurchaseByCustId(@PathVariable long id) {
        List<PurchaseDetailsDTO> dto = purchaseService.getPurchaseByCustId(id);
        return ResponseEntity.ok(dto);
    }


    @GetMapping("/today")
    public ResponseEntity<List<TaskDTO>> getTodayPurchases() {
        return ResponseEntity.ok(purchaseService.getTodayPurchases());
    }

    @GetMapping("/month")
    public ResponseEntity<List<TaskDTO>> getThisMonthPurchases() {
        return ResponseEntity.ok(purchaseService.getPurchasesThisMonth());
    }

    @GetMapping("/range")
    public ResponseEntity<List<TaskDTO>> getPurchasesByRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);
        return ResponseEntity.ok(purchaseService.getPurchasesByRange(start, end));
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

    @PutMapping("/update/{id}")
    public ResponseEntity<CustomerPurchases> updatePurchase(@PathVariable long id, @RequestBody CustomerPurchases purchase){
        return ResponseEntity.ok(purchaseService.updatePurchase(id, purchase));
    }

    @GetMapping("/notes/{id}")
    public ResponseEntity<?> getNotes(@PathVariable long id){
        noteService.getNotes(id);
        return ResponseEntity.ok(noteService.getNotes(id));
    }
}
