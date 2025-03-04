package com.aw.midtrans_integration.controllers;

import com.aw.midtrans_integration.dtos.requests.CreateOrderRequest;
import com.aw.midtrans_integration.dtos.responses.CreateOrderResponse;
import com.aw.midtrans_integration.entities.Order;
import com.aw.midtrans_integration.services.MidtransService;
import com.aw.midtrans_integration.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MidtransService midtransService;

    @PostMapping("/snap")
    public CreateOrderResponse createSnapOrder(@RequestBody CreateOrderRequest request) throws Exception {
        Order savedOrder = orderService.createOrder(request);

        String snapToken = midtransService.createSnapTransaction(savedOrder).getString("token");
        String redirectUrl = midtransService.createSnapTransaction(savedOrder).getString("redirect_url");

        CreateOrderResponse response = new CreateOrderResponse();
        response.setToken(snapToken);
        response.setRedirectUrl(redirectUrl);
        return response;
    }

    @GetMapping("/{id}")
    public Order getOrder(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }
}