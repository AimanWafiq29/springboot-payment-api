package com.aw.midtrans_integration.services;

import com.aw.midtrans_integration.entities.Order;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.service.MidtransSnapApi;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

    @Service
public class MidtransService {

    @Autowired
    private MidtransSnapApi midtransSnapApi;

    public JSONObject createSnapTransaction(Order order) throws MidtransError {
        Map<String, Object> params = new HashMap<>();

        Map<String, String> transactionDetails = new HashMap<>();
        transactionDetails.put("order_id", order.getId().toString());
        transactionDetails.put("gross_amount", String.valueOf(order.getPrice() * order.getQuantity()));

        Map<String, Object> itemDetails = new HashMap<>();
        itemDetails.put("id", order.getId().toString());
        itemDetails.put("price", order.getPrice());
        itemDetails.put("quantity", order.getQuantity());
        itemDetails.put("name", order.getProductName());

        params.put("transaction_details", transactionDetails);
        params.put("item_details", itemDetails);

        return midtransSnapApi.createTransaction(params);
    }
}