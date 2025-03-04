package com.aw.midtrans_integration.repositories;

import com.aw.midtrans_integration.entities.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {
    PaymentHistory findByOrderId(Long orderId);
}