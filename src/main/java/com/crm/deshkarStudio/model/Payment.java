package com.crm.deshkarStudio.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long paymentId;
//    @ManyToOne
//    @JoinColumn(name = "purchase_id", nullable = false)
    private long purchaseId;
    private double amount;
    private LocalDate paymentDate;
    private String paymentType;

}
