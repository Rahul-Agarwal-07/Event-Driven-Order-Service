package com.rahulagarwal.orderservice.domain.model.order;

import java.util.UUID;

public class OrderId {
    private final UUID id;

    public OrderId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
