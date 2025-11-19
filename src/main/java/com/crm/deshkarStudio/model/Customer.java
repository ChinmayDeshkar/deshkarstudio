package com.crm.deshkarStudio.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;
    private String email;

    @Column(unique = true, nullable = false)
    private String phoneNumber;

    private String address;
    private LocalDateTime createdDate = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @ToString.Exclude
    @JsonIgnore
    private List<CustomerPurchases> purchases;

    private long purchaseCount = 0;
}