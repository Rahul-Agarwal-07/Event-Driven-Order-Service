package com.rahulagarwal.orderservice.domain.model.order;

import com.rahulagarwal.orderservice.domain.model.shared.DomainEvent;
import com.rahulagarwal.orderservice.domain.model.shared.UserId;

import java.time.Instant;

public class OrderCreatedEvent implements DomainEvent {

    private final OrderId orderId;
    private final UserId userId;
    private final Instant occurredAt;

    public OrderCreatedEvent(OrderId orderId, UserId userId, Instant occurredAt) {
        this.orderId = orderId;
        this.userId = userId;
        this.occurredAt = occurredAt;
    }

    @Override
    public String getEventType() {
        return "ORDER_CREATED";
    }

    @Override
    public Instant getOccurredAt() {
        return occurredAt;
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public UserId getUserId() {
        return userId;
    }
}
