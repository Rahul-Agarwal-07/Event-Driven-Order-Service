package com.rahulagarwal.orderservice.domain.model.outbox;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record EventId(UUID id) {

    @JsonCreator
    public EventId(@JsonProperty("id") UUID id) {
        this.id = id;
    }
}