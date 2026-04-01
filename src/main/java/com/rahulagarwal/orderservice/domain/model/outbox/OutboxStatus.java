package com.rahulagarwal.orderservice.domain.model.outbox;

public enum OutboxStatus {
    PENDING,
    PROCESSING,
    PROCESSED,
    RETRYABLE,
    DEAD
}
