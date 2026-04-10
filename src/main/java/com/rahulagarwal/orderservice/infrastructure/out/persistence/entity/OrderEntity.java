package com.rahulagarwal.orderservice.infrastructure.out.persistence.entity;

import com.rahulagarwal.orderservice.domain.model.order.OrderItem;
import com.rahulagarwal.orderservice.domain.model.order.OrderStatus;
import com.rahulagarwal.orderservice.domain.model.order.PaymentStatus;
import com.rahulagarwal.orderservice.domain.model.shared.Money;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    private UUID orderId;

    @Column(nullable = false)
    private UUID userId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> orderItems;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    private Instant createdAt;

    private Instant updatedAt;

    protected OrderEntity() {}

    public OrderEntity(
            UUID orderId,
            UUID userId,
            List<OrderItemEntity> orderItems,
            OrderStatus orderStatus,
            PaymentStatus paymentStatus,
            BigDecimal totalAmount,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderItems = orderItems;
        this.orderStatus = orderStatus;
        this.paymentStatus = paymentStatus;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    //Getters

    public UUID getOrderId() {
        return orderId;
    }

    public UUID getUserId() {
        return userId;
    }

    public List<OrderItemEntity> getOrderItems() {
        return orderItems;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    // Setters
    public void setOrderItems(List<OrderItemEntity> orderItems) {
        this.orderItems = orderItems;
    }
}