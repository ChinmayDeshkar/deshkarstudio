package com.crm.deshkarStudio.contoller;

import com.crm.deshkarStudio.services.impl.EmailServiceImpl;
import com.crm.deshkarStudio.services.impl.SalesReportServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api")
public class DemoController {

    @Autowired
    SalesReportServiceImpl salesReportService;

    @GetMapping("/admin/hello")
    public ResponseEntity<String> adminHello() { return ResponseEntity.ok("Hello Admin"); }

    @GetMapping("/employee/hello")
    public ResponseEntity<String> employeeHello() { return ResponseEntity.ok("Hello Employee"); }

    @GetMapping("/user/hello")
    public ResponseEntity<String> userHello() { return ResponseEntity.ok("Hello Authenticated User"); }

    @GetMapping("/ping")
    public String ping() {
        return "OK";
    }

    @GetMapping("/test")
    public ResponseEntity<?> testReport(){

        salesReportService.sendDailySalesReport();
        return ResponseEntity.ok("Mail sent");
    }
}
