package com.rahulagarwal.orderservice.domain.model.outbox;

import java.util.UUID;

public class AggregateId {

    private final String value;

    public AggregateId(Object id) {
        this.value = id.toString();
    }

    public String getValue() {
        return value;
    }
}
