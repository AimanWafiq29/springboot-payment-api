package com.aw.midtrans_integration.services;

import com.aw.midtrans_integration.dtos.requests.CreateOrderRequest;
import com.aw.midtrans_integration.entities.Order;
import com.aw.midtrans_integration.entities.PaymentHistory;
import com.aw.midtrans_integration.repositories.OrderRepository;
import com.aw.midtrans_integration.repositories.PaymentHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentHistoryRepository paymentHistoryRepository;

    public Order createOrder(CreateOrderRequest request) {

        Order order = new Order();
        order.setOrderId(request.getOrderId());
        order.setProductName(request.getProductName());
        order.setPrice(request.getPrice());
        order.setQuantity(request.getQuantity());
        order.setStatus("PENDING");

        Order savedOrder = orderRepository.save(order); // Simpan order

        // Simpan ke PaymentHistory
        PaymentHistory paymentHistory = new PaymentHistory();
        paymentHistory.setOrder(savedOrder); // Relasi ke order
        paymentHistory.setGrossAmount(savedOrder.getPrice()); // Harga dari order
        paymentHistory.setPaymentMethod(null); // Belum ada metode pembayaran
        paymentHistory.setTransactionId(null); // Belum ada ID transaksi
        paymentHistory.setTransactionStatus("PENDING"); // Default PENDING
        paymentHistory.setTransactionTime(LocalDateTime.now()); // Waktu transaksi

        paymentHistoryRepository.save(paymentHistory); // Simpan ke database

        return orderRepository.save(order);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
}