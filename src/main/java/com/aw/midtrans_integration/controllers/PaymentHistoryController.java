package com.aw.midtrans_integration.controllers;

import com.aw.midtrans_integration.entities.PaymentHistory;
import com.aw.midtrans_integration.services.PaymentHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment-history")
public class PaymentHistoryController {

    @Autowired
    private PaymentHistoryService paymentHistoryService;

    @GetMapping("/")
    public List<PaymentHistory> getAllPaymentHistories() {
        return paymentHistoryService.getAllPaymentHistories();
    }

    @GetMapping("/{orderId}")
    public List<PaymentHistory> getPaymentHistoryByOrderId(@PathVariable String orderId) {
        return paymentHistoryService.getPaymentHistoryByOrderId(orderId);
    }
}
