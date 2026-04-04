package com.rahulagarwal.orderservice.domain.model.outbox;

public enum OutboxStatus {
    PENDING,
    PROCESSING,
    PUBLISHED,
    RETRYABLE,
    DEAD
}
