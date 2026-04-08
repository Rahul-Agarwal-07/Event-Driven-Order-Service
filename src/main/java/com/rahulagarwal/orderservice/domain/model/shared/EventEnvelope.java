package com.rahulagarwal.orderservice.domain.model.shared;

import com.rahulagarwal.orderservice.domain.model.outbox.AggregateId;
import com.rahulagarwal.orderservice.domain.model.outbox.EventId;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EventEnvelope {

    private final EventId eventId;
    private final AggregateId aggregateId;
    private final String aggregateType;
    private final String eventType;
    private final String payload;
    private final Instant occurredAt;
    private final Instant createdAt;

    @JsonCreator
    public EventEnvelope(
            @JsonProperty("eventId") EventId eventId,
            @JsonProperty("aggregateId") AggregateId aggregateId,
            @JsonProperty("aggregateType") String aggregateType,
            @JsonProperty("eventType") String eventType,
            @JsonProperty("payload") String payload,
            @JsonProperty("occurredAt") Instant occurredAt,
            @JsonProperty("createdAt") Instant createdAt
    ) {
        this.eventId = eventId;
        this.aggregateId = aggregateId;
        this.aggregateType = aggregateType;
        this.eventType = eventType;
        this.payload = payload;
        this.occurredAt = occurredAt;
        this.createdAt = createdAt;
    }

    // getters
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
