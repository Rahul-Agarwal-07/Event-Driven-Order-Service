package com.rahulagarwal.orderservice.domain.model.order;
import com.rahulagarwal.orderservice.domain.exception.InvalidStateException;
import com.rahulagarwal.orderservice.domain.model.shared.Money;

import java.util.UUID;

public class OrderItem {

    private final UUID productId;
    private final int quantity;
    private final Money price;

    public OrderItem(UUID productId, int quantity, Money price) {
        if (quantity <= 0) throw new InvalidStateException("Quantity must be > 0");

        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public OrderItem withQuantity(int newQty) {
        return new OrderItem(this.productId, newQty, this.price);
    }

    public UUID getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public Money getPrice() {
        return price;
    }
}