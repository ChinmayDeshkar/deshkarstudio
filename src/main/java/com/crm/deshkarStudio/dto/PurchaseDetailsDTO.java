package com.crm.deshkarStudio.dto;

import com.crm.deshkarStudio.model.Customer;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@ToString
@Getter
@Setter
public class PurchaseDetailsDTO {

    private Long purchaseId;
    private Customer customer;
    private double price;
    private String paymentMethod;
    private String paymentStatus;
    private String orderStatus;
    private double advancePaid;
    private double balance;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdDate = LocalDateTime.now();
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime updatedDate;
    private String updatedBy;
    private String remarks;

}
