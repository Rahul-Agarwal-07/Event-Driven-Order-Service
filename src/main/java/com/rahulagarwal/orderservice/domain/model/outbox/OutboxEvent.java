package com.rahulagarwal.orderservice.domain.model.outbox;

import java.time.Instant;

public class OutboxEvent {

    private final EventId eventId;
    private final AggregateId aggregateId;
    private final String aggregateType;
    private final String eventType;

    private final String payload;
    private final OutboxStatus status;

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
            OutboxStatus status,
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
        this.status = status;
        this.occurredAt = occurredAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.retryCount = retryCount;
        this.errorMessage = errorMessage;
    }

    public EventId getEventId() {
        return eventId;
    }

    public AggregateId getAggregateId() {
        return aggregateId;
    }

    public String getAggregateType() {
        return aggregateType;
    }

    public String getEventType() {
        return eventType;
    }

    public String getPayload() {
        return payload;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public OutboxStatus getStatus() {
        return status;
    }
}
