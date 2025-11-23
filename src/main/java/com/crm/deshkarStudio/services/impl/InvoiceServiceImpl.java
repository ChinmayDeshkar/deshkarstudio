package com.crm.deshkarStudio.services.impl;

import com.crm.deshkarStudio.model.CustomerPurchases;
import com.crm.deshkarStudio.model.Invoice;
import com.crm.deshkarStudio.pdf.InvoicePdfGenerator;
import com.crm.deshkarStudio.repo.CustomerPurchasesRepo;
import com.crm.deshkarStudio.repo.InvoiceRepo;
import com.crm.deshkarStudio.services.InvoiceService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@AllArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepo invoiceRepo;
    private final CustomerPurchasesRepo purchaseRepo;

    @Override
    public Invoice generateInvoice(Long purchaseId) throws Exception {

        CustomerPurchases p = purchaseRepo.findById(purchaseId)
                .orElseThrow(() -> new RuntimeException("Purchase not found"));

        Optional<Invoice> existingOpt = invoiceRepo.findByPurchaseId(purchaseId);

        Invoice inv;
        if (existingOpt.isPresent()) {
            // Update existing invoice
            inv = existingOpt.get();
            log.debug("Updating existing invoice: " + inv.getInvoiceNumber());
        } else {
            // Create new invoice
            inv = new Invoice();
            inv.setInvoiceDate(LocalDateTime.now());
            inv.setPurchaseId(purchaseId);

            // Auto-generate invoice number
            String invoiceNumber = "INV-" +
                    LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) +
                    "-" + String.format("%04d", invoiceRepo.findAll().size() + 1);
            inv.setInvoiceNumber(invoiceNumber);

            log.debug("Creating new invoice: " + invoiceNumber);
        }

        // Update common fields (both new and existing)
        inv.setCustomerName(p.getCustomer().getCustomerName());
        inv.setCustomerPhone(p.getCustomer().getPhoneNumber());
        inv.setCustomerEmail(p.getCustomer().getEmail());
        inv.setTotalAmount(p.getPrice());
        inv.setPaidAmount(p.getAdvancePaid());
        inv.setBalanceAmount(p.getBalance());
        inv.setPaymentMethod(p.getPaymentMethod());
        inv.setStatus("GENERATED");

        // Generate PDF if needed
//    String filePath = InvoicePdfGenerator.generatePDF(inv, p);
//    inv.setPdfFilePath(filePath);
//    log.debug("Invoice PDF updated/created at " + filePath);

        return invoiceRepo.save(inv);
    }


//    @Override
//    public byte[] downloadInvoice(Long purchaseId) throws Exception {
//        Invoice inv = invoiceRepo.findByPurchaseId(purchaseId)
//                .orElseThrow(() -> new RuntimeException("No invoice found"));
//
//        return Files.readAllBytes(Path.of(inv.getPdfFilePath()));
//    }

    @Override
    public byte[] downloadInvoice(Long purchaseId) throws Exception {

        CustomerPurchases p = purchaseRepo.findById(purchaseId)
                .orElseThrow(() -> new RuntimeException("Purchase not found"));

        Invoice inv = invoiceRepo.findByPurchaseId(purchaseId)
                .orElseGet(() -> {
                    log.error("Invoice not found!! Purchase id: " + purchaseId);

                    try {
                        return generateInvoice(purchaseId);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        return InvoicePdfGenerator.generatePDF(inv, p);
    }
}
