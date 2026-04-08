package com.rahulagarwal.orderservice.domain.model.consumer;

import com.rahulagarwal.orderservice.domain.model.outbox.EventId;

import java.time.Instant;

public class ProcessedEvent {
    private final EventId eventId;
    private final Instant processedAt;

    public ProcessedEvent(EventId eventId, Instant processedAt) {
        this.eventId = eventId;
        this.processedAt = processedAt;
    }

    public EventId getEventId() {
        return eventId;
    }

    public Instant getProcessedAt() {
        return processedAt;
    }
}
