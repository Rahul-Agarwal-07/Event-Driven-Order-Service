package com.rahulagarwal.orderservice.domain.model.shared;

import java.time.Instant;

public interface DomainEvent {

    String getEventType();
    Instant getOccurredAt();

}
