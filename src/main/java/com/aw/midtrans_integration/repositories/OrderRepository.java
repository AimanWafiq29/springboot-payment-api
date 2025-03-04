package com.aw.midtrans_integration.repositories;

import com.aw.midtrans_integration.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByOrderId(String orderId); // Pastikan field orderId ada di entity Order
}