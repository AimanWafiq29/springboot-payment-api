package com.aw.midtrans_integration.repositories;

import com.aw.midtrans_integration.entities.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {
    @Query("SELECT p FROM PaymentHistory p WHERE p.order.orderId = :orderId")
    List<PaymentHistory> findByOrder(@Param("orderId") String orderId);
}