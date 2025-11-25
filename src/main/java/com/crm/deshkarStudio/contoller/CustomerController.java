package com.crm.deshkarStudio.contoller;

import com.crm.deshkarStudio.model.CustomerPurchases;
import com.crm.deshkarStudio.services.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/check")
    public ResponseEntity<?> checkCustomer(@RequestParam String phoneNumber) {
        log.debug("Checking customer details for number: " + phoneNumber);
        boolean exists = customerService.customerExists(phoneNumber);

        if (exists) {
            return  customerService.customerDetails(phoneNumber);
        } else {
            return ResponseEntity.ok(Map.of("exists", false));
        }
    }

    @GetMapping("/test")
    public ResponseEntity<?> test(){
        return ResponseEntity.ok(Map.of("test", "Success"));
    }

    @PostMapping("/purchase")
    public ResponseEntity<CustomerPurchases> addPurchase(@RequestBody Map<String, Object> payload) {
        return ResponseEntity.ok(customerService.addPurchase(payload));
    }
}
