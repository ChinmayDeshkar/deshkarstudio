package com.crm.deshkarStudio.contoller;

import com.crm.deshkarStudio.model.Invoice;
import com.crm.deshkarStudio.pdf.InvoicePdfGenerator;
import com.crm.deshkarStudio.repo.InvoiceRepo;
import com.crm.deshkarStudio.services.InvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/invoice")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final InvoiceRepo invoiceRepo;

    @PostMapping("/generate/{purchaseId}")
    public ResponseEntity<?> generateInvoice(@PathVariable Long purchaseId) throws Exception {
        log.info("Generating Invoice for purchaseId: " + purchaseId);
        Invoice inv = invoiceService.generateInvoice(purchaseId);
        return ResponseEntity.ok(inv);
    }

    @GetMapping("/download/{purchaseId}")
    public ResponseEntity<byte[]> download(@PathVariable Long purchaseId) throws Exception {
        byte[] pdf = invoiceService.downloadInvoice(purchaseId);

        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "attachment; filename=invoice.pdf")
                .body(pdf);
    }

//    @GetMapping("/test/{invoiceId}")
//    public ResponseEntity<byte[]> testInvoice(@PathVariable Long invoiceId) throws Exception {
//        Invoice inv = invoiceRepo.findById(invoiceId).orElseThrow();
//        byte[] pdf = InvoicePdfGenerator.generatePDF(inv);
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice-" + inv.getInvoiceNumber() + ".pdf")
//                .contentType(MediaType.APPLICATION_PDF)
//                .body(pdf);
//    }
}
