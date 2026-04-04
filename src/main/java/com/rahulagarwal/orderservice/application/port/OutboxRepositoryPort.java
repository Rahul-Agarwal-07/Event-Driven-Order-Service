package com.rahulagarwal.orderservice.application.port;

import com.rahulagarwal.orderservice.domain.model.outbox.EventId;
import com.rahulagarwal.orderservice.domain.model.outbox.OutboxEvent;

import java.util.List;

public interface OutboxRepositoryPort {

    void save(OutboxEvent event);
    void markAsPublished(EventId eventId);
    void markAsFailed(EventId event, String errorMessage);
    List<OutboxEvent> fetchAndMarkProcessing(int batchSize, String workerId);


}
