package com.aw.midtrans_integration.dtos.requests;

import lombok.Data;

@Data
public class CreateOrderRequest {
    private String orderId;
    private Double grossAmount;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
}
