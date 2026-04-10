package com.rahulagarwal.orderservice.infrastructure.out.persistence.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "order_items")
public class OrderItemEntity {

    @Id
    private UUID id;

    private UUID productId;

    private BigDecimal price;

    private int quantity;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    protected OrderItemEntity() {}

    public OrderItemEntity(UUID id, UUID productId, BigDecimal price, int quantity, OrderEntity order) {
        this.id = id;
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
        this.order = order;
    }

    // Getters


    public UUID getId() {
        return id;
    }

    public UUID getProductId() {
        return productId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public OrderEntity getOrder() {
        return order;
    }
}