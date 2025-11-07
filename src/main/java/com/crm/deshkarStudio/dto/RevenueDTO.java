package com.crm.deshkarStudio.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RevenueDTO {
    private String label;   // e.g. "Jan 2025", "2025-11-07", "Cash"
    private double income;
    private double balance;
//    private long transactions;
}