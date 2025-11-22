package com.crm.deshkarStudio.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invoiceId;

    private String invoiceNumber;   // Auto-generated like INV-202501-0001

    private Long purchaseId;

    private String customerName;
    private String customerPhone;
    private String customerEmail;

    private LocalDateTime invoiceDate;

    private Double totalAmount;
    private Double paidAmount;
    private Double balanceAmount;
    private String paymentMethod;

    private String status; // GENERATED, DELIVERED

    private String pdfFilePath; // Saved file path
}
