package com.rahulagarwal.orderservice.domain.model.outbox;

import java.time.Instant;

public class OutboxEvent {

    private final EventId eventId;
    private final AggregateId aggregateId;
    private final String aggregateType;
    private final String eventType;

    private final String payload;

    private final Instant occurredAt;
    private final Instant createdAt;
    private final Instant updatedAt;

    private final Integer retryCount;
    private final String errorMessage;

    public OutboxEvent(
            EventId eventId,
            AggregateId aggregateId,
            String aggregateType,
            String eventType,
            String payload,
            Instant occurredAt,
            Instant createdAt,
            Instant updatedAt,
            Integer retryCount,
            String errorMessage
    ) {
        this.eventId = eventId;
        this.aggregateId = aggregateId;
        this.aggregateType = aggregateType;
        this.eventType = eventType;
        this.payload = payload;
        this.occurredAt = occurredAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.retryCount = retryCount;
        this.errorMessage = errorMessage;
    }

}
