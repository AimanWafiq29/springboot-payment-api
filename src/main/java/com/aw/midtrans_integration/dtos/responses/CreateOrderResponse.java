package com.aw.midtrans_integration.dtos.responses;

import lombok.Data;

@Data
public class CreateOrderResponse {
    private String token;
    private String redirectUrl;
}