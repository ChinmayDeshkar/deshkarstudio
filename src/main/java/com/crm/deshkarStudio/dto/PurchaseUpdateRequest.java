package com.crm.deshkarStudio.dto;


import com.crm.deshkarStudio.model.Customer;
import com.crm.deshkarStudio.model.PurchaseItems;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class PurchaseUpdateRequest {

    private Long purchaseId;
    private Customer customer;
    private List<PurchaseItems> items;
    private double price;
    private String paymentMethod;
    private String paymentStatus;
    private String orderStatus;
    private double advancePaid;
    private double balance;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdDate;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime updatedDate;
    private String updatedBy;
    private String remarks;
    boolean customerUpdated;

}
