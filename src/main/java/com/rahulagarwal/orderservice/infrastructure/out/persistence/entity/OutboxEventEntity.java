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

    private Integer eventVersion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OutboxStatus status;

    @Column(nullable = false)
    private Instant occurredAt;

    @Column(nullable = false)
    private Instant createdAt;

    private Instant processedAt;

    private Integer retryCount;
    private Instant nextRetryAt;

    @Column(length = 1000)
    private String errorMessage;

    @Column(columnDefinition = "jsonb", nullable = false)
    private String payload;
}