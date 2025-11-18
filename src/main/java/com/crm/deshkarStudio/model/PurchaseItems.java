package com.crm.deshkarStudio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @ManyToOne
    @JoinColumn(name = "purchase_id")
    private CustomerPurchases purchase;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Products product;

    private int quantity;

    private double itemPrice; // allow override if needed (e.g. discount)

    private double total; // quantity * itemPrice
}
