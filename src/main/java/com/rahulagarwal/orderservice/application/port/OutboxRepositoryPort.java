package com.rahulagarwal.orderservice.application.port;

import com.rahulagarwal.orderservice.domain.model.outbox.OutboxEvent;

public interface OutboxRepositoryPort {

    void save(OutboxEvent event);

}
