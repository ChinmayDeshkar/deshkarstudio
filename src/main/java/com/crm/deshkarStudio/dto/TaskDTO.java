package com.crm.deshkarStudio.dto;

import com.crm.deshkarStudio.model.Customer;
import com.crm.deshkarStudio.model.PurchaseItems;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TaskDTO {
    private long purchaseId;
    private String customerName;
    private String phoneNumber;
    private double price;
    private double balance;
    private String paymentStatus;
    private String orderStatus;
    private String remark;
    private LocalDateTime dte_created;

}
