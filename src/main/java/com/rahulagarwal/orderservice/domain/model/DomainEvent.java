package com.rahulagarwal.orderservice.domain.model;

import java.time.Instant;

public interface DomainEvent {

    String getEventType();
    Instant getOccurredAt();

}
