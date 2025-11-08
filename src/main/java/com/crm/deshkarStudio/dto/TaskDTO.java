package com.crm.deshkarStudio.dto;

import lombok.*;

import java.time.LocalDateTime;

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
