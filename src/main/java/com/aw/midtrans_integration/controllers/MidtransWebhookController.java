package com.aw.midtrans_integration.controllers;

import com.aw.midtrans_integration.entities.Order;
import com.aw.midtrans_integration.entities.PaymentHistory;
import com.aw.midtrans_integration.repositories.OrderRepository;
import com.aw.midtrans_integration.repositories.PaymentHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/midtrans")
public class MidtransWebhookController {

    private final PaymentHistoryRepository paymentHistoryRepository;
    private final OrderRepository orderRepository;
    private static final Logger logger = LoggerFactory.getLogger(MidtransWebhookController.class);

    public MidtransWebhookController(PaymentHistoryRepository paymentHistoryRepository, OrderRepository orderRepository) {
        this.paymentHistoryRepository = paymentHistoryRepository;
        this.orderRepository = orderRepository;
    }

    @PostMapping("/notification")
    public ResponseEntity<String> handleNotification(@RequestBody Map<String, Object> payload) {
        try {
            logger.info("Received webhook: {}", payload);

            String orderId = (String) payload.get("order_id");
            String transactionStatus = (String) payload.get("transaction_status");
            String transactionId = (String) payload.get("transaction_id");
            Double grossAmount = payload.get("gross_amount") != null ? Double.valueOf(payload.get("gross_amount").toString()) : null;
            String paymentMethod = (String) payload.get("payment_type");
            LocalDateTime transactionTime = LocalDateTime.now();

            if (orderId == null || transactionStatus == null) {
                logger.error("Invalid payload: missing order_id or transaction_status");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid payload");
            }

            // Cek apakah Order ada
            Optional<Order> optionalOrder = orderRepository.findByOrderId(orderId);
            if (optionalOrder.isEmpty()) {
                logger.error("Order not found: {}", orderId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
            }

            Order order = optionalOrder.get();

            // Cek apakah PaymentHistory sudah ada
            List<PaymentHistory> paymentHistories = paymentHistoryRepository.findByOrder(orderId);
            PaymentHistory paymentHistory = paymentHistories.isEmpty() ? new PaymentHistory() : paymentHistories.get(0);

            // Jika baru, set order
            if (paymentHistories.isEmpty()) {
                paymentHistory.setOrder(order);
            }

            // Update data PaymentHistory
            paymentHistory.setGrossAmount(grossAmount);
            paymentHistory.setPaymentMethod(paymentMethod);
            paymentHistory.setTransactionId(transactionId);
            paymentHistory.setTransactionStatus(transactionStatus);
            paymentHistory.setTransactionTime(transactionTime);

            // Update status Order berdasarkan transaksi
            switch (transactionStatus) {
                case "settlement":
                    order.setStatus("PAID");
                    break;
                case "pending":
                    order.setStatus("PENDING");
                    break;
                case "deny":
                case "expire":
                case "cancel":
                    order.setStatus("FAILED");
                    break;
                default:
                    order.setStatus("UNKNOWN");
                    break;
            }

            // Simpan ke database
            orderRepository.save(order);
            paymentHistoryRepository.save(paymentHistory);

            logger.info("Order {} updated with status {}", orderId, order.getStatus());
            logger.info("Payment history updated for order {}", orderId);

            return ResponseEntity.ok("Webhook processed");
        } catch (Exception e) {
            logger.error("Error processing webhook", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing webhook");
        }
    }
}