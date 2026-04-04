package com.rahulagarwal.orderservice.infrastructure.out.persistence.entity;

import com.rahulagarwal.orderservice.domain.model.outbox.OutboxStatus;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;
@Entity
@Table(
        name = "outbox_table",
        indexes = {
                @Index(name = "idx_outbox_status", columnList = "status"),
                @Index(name = "idx_outbox_next_retry", columnList = "nextRetryAt"),
                @Index(name = "idx_outbox_created_at", columnList = "createdAt")
        }
)
public class OutboxEventEntity {

    @Id
    private UUID eventId;

    @Column(nullable = false)
    private String aggregateId;

    @Column(nullable = false)
    private String aggregateType;

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false)
    private Integer eventVersion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OutboxStatus status;

    @Column(nullable = false)
    private Instant occurredAt;

    @Column(nullable = false)
    private Instant createdAt;

    private Instant updatedAt;

    private Instant processedAt;
    private String processedBy;

    @Column(nullable = false)
    private Integer retryCount = 0;

    @Column(nullable = false)
    private Instant nextRetryAt;

    @Column(length = 1000)
    private String errorMessage;

    @Column(columnDefinition = "jsonb", nullable = false)
    private String payload;

    // Constructor

    public OutboxEventEntity(
            UUID eventId,
            String aggregateId,
            String aggregateType,
            String eventType,
            Integer eventVersion,
            OutboxStatus status,
            Instant occurredAt,
            Instant createdAt,
            Instant updatedAt,
            Instant processedAt,
            String processedBy,
            Integer retryCount,
            Instant nextRetryAt,
            String errorMessage,
            String payload
    ) {
        this.eventId = eventId;
        this.aggregateId = aggregateId;
        this.aggregateType = aggregateType;
        this.eventType = eventType;
        this.eventVersion = eventVersion;
        this.status = status;
        this.occurredAt = occurredAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.processedAt = processedAt;
        this.processedBy = processedBy;
        this.retryCount = retryCount;
        this.nextRetryAt = nextRetryAt;
        this.errorMessage = errorMessage;
        this.payload = payload;
    }

    // Setters

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public void setAggregateId(String aggregateId) {
        this.aggregateId = aggregateId;
    }

    public void setAggregateType(String aggregateType) {
        this.aggregateType = aggregateType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setEventVersion(Integer eventVersion) {
        this.eventVersion = eventVersion;
    }

    public void setStatus(OutboxStatus status) {
        this.status = status;
    }

    public void setOccurredAt(Instant occurredAt) {
        this.occurredAt = occurredAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setProcessedAt(Instant processedAt) {
        this.processedAt = processedAt;
    }

    public void setProcessedBy(String processedBy) {
        this.processedBy = processedBy;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public void setNextRetryAt(Instant nextRetryAt) {
        this.nextRetryAt = nextRetryAt;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Getters

    public UUID getEventId() {
        return eventId;
    }

    public String getAggregateId() {
        return aggregateId;
    }

    public String getAggregateType() {
        return aggregateType;
    }

    public String getEventType() {
        return eventType;
    }

    public Integer getEventVersion() {
        return eventVersion;
    }

    public OutboxStatus getStatus() {
        return status;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getProcessedAt() {
        return processedAt;
    }

    public String getProcessedBy() {
        return processedBy;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public Instant getNextRetryAt() {
        return nextRetryAt;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getPayload() {
        return payload;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}