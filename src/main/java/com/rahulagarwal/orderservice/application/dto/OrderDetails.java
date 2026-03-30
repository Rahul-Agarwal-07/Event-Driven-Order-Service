package com.rahulagarwal.orderservice.application.dto;

import java.util.UUID;

public class OrderDetails {
    UUID productId;
    int quantity;

    public OrderDetails(UUID productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public UUID getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
