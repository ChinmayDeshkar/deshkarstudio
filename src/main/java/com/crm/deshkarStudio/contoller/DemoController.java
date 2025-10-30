package com.crm.deshkarStudio.contoller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api")
public class DemoController {

    @GetMapping("/admin/hello")
    public ResponseEntity<String> adminHello() { return ResponseEntity.ok("Hello Admin"); }

    @GetMapping("/employee/hello")
    public ResponseEntity<String> employeeHello() { return ResponseEntity.ok("Hello Employee"); }

    @GetMapping("/user/hello")
    public ResponseEntity<String> userHello() { return ResponseEntity.ok("Hello Authenticated User"); }
}
