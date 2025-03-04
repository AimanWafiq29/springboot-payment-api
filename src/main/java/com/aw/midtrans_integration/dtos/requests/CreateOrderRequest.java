package com.aw.midtrans_integration.dtos.requests;

import lombok.Data;

@Data
public class CreateOrderRequest {
    private String orderId;
    private String productName;
    private Double price;
    private Integer quantity;
    private String status;
}
