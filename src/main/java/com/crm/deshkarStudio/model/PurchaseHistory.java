package com.crm.deshkarStudio.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PurchaseHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private Long purchaseId;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonBackReference
    private Customer customer;

    private double price;
    private String paymentMethod;
    private String paymentStatus;
    private String orderStatus;
    private double advancePaid;
    private double balance;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime updatedDate;
    private String updatedBy;
    private String remarks;
}
