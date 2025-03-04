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
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/midtrans")
public class MidtransWebhookController {

    @Autowired
    private PaymentHistoryRepository paymentHistoryRepository;

    @Autowired
    private OrderRepository orderRepository;

    private static final Logger logger = LoggerFactory.getLogger(MidtransWebhookController.class);

    @PostMapping("/notification")
    public ResponseEntity<String> handleNotification(@RequestBody Map<String, Object> payload) {
        try {
            logger.info("Received webhook: {}", payload);

            String orderId = (String) payload.get("order_id");
            String transactionStatus = (String) payload.get("transaction_status");

            if (orderId == null || transactionStatus == null) {
                logger.error("Invalid payload: missing order_id or transaction_status");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid payload");
            }

            Order order = orderRepository.findByOrderId(orderId);
            if (order == null) {
                logger.error("Order not found: {}", orderId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
            }

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

            orderRepository.save(order);
            logger.info("Order {} updated with status {}", orderId, order.getStatus());

            return ResponseEntity.ok("Webhook processed");

        } catch (Exception e) {
            logger.error("Error processing webhook", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing webhook");
        }
    }

}
