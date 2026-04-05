package com.rahulagarwal.orderservice.domain.model.shared;

import com.rahulagarwal.orderservice.domain.model.outbox.AggregateId;
import com.rahulagarwal.orderservice.domain.model.outbox.EventId;
import com.rahulagarwal.orderservice.domain.model.outbox.OutboxStatus;

import java.time.Instant;

public class EventEnvelope {

    private final EventId eventId;
    private final AggregateId aggregateId;
    private final String aggregateType;
    private final String eventType;
    private final String payload;
    private final Instant occurredAt;
    private final Instant createdAt;

    public EventEnvelope(
            EventId eventId,
            AggregateId aggregateId,
            String aggregateType,
            String eventType,
            String payload,
            Instant occurredAt,
            Instant createdAt
    ) {
        this.eventId = eventId;
        this.aggregateId = aggregateId;
        this.aggregateType = aggregateType;
        this.eventType = eventType;
        this.payload = payload;
        this.occurredAt = occurredAt;
        this.createdAt = createdAt;
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
}
