package com.aw.midtrans_integration.services;

import com.aw.midtrans_integration.dtos.requests.CreateOrderRequest;
import com.aw.midtrans_integration.entities.Order;
import com.aw.midtrans_integration.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order createOrder(CreateOrderRequest request) {
        Order order = new Order();
        order.setOrderId(request.getOrderId());
        order.setProductName("iPhone 14");
        order.setPrice(request.getGrossAmount());
        order.setQuantity(1);
        order.setStatus("PENDING");

        return orderRepository.save(order);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
}