package com.rahulagarwal.orderservice.application.port;

import com.rahulagarwal.orderservice.domain.model.consumer.ProcessedEvent;
import com.rahulagarwal.orderservice.domain.model.outbox.EventId;

public interface ProcessedEventRepositoryPort {

    void save(ProcessedEvent event);

}
