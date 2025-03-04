package com.aw.midtrans_integration.services;

import com.aw.midtrans_integration.entities.Order;
import com.aw.midtrans_integration.entities.PaymentHistory;
import com.aw.midtrans_integration.repositories.OrderRepository;
import com.aw.midtrans_integration.repositories.PaymentHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentHistoryService {

    @Autowired
    private PaymentHistoryRepository paymentHistoryRepository;

    @Autowired
    private OrderRepository orderRepository;

    public List<PaymentHistory> getAllPaymentHistories() {
        return paymentHistoryRepository.findAll();
    }

    public List<PaymentHistory> getPaymentHistoryByOrderId(String orderId) {

        Optional<Order> order = orderRepository.findByOrderId(orderId);

        if (order.isEmpty()) {
            throw new RuntimeException("Order not found with ID: " + orderId);
        }

        return paymentHistoryRepository.findByOrder(order.get().getOrderId());
    }
}