package com.rahulagarwal.orderservice.infrastructure.out.persistence.mapper;

import com.rahulagarwal.orderservice.domain.model.outbox.AggregateId;
import com.rahulagarwal.orderservice.domain.model.outbox.EventId;
import com.rahulagarwal.orderservice.domain.model.outbox.OutboxEvent;
import com.rahulagarwal.orderservice.domain.model.outbox.OutboxStatus;
import com.rahulagarwal.orderservice.infrastructure.out.persistence.entity.OutboxEventEntity;

public class OutboxMapper {

    public static OutboxEvent toDomain(OutboxEventEntity entity)
    {
        return new OutboxEvent(
                new EventId(entity.getEventId()),
                new AggregateId(entity.getAggregateId()),
                entity.getAggregateType(),
                entity.getEventType(),
                entity.getPayload(),
                entity.getStatus(),
                entity.getOccurredAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getRetryCount(),
                entity.getErrorMessage()
        );
    }

    public static OutboxEventEntity toEntity(OutboxEvent event)
    {
        return new OutboxEventEntity(
                event.getEventId().id(),
                event.getAggregateId().getValue(),
                event.getAggregateType(),
                event.getEventType(),
                1,
                event.getStatus(),
                event.getOccurredAt(),
                event.getCreatedAt(),
                event.getUpdatedAt(),
                null,
                null,
                event.getRetryCount(),
                null,
                event.getErrorMessage(),
                event.getPayload()
        );
    }

}