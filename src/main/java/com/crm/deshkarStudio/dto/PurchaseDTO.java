package com.crm.deshkarStudio.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class PurchaseDTO {
    private Long purchaseId;
    private String customerName;
    private String phoneNumber;
    private Double price;
    private String paymentMethod;
    private String paymentStatus;
    private Double balance;
    private LocalDateTime createdDate;
    private String remarks;

}
