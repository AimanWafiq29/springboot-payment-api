package com.aw.midtrans_integration.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderId; // Tambahkan field ini
    private String productName;
    private Double price;
    private Integer quantity;
    private String status;
}
