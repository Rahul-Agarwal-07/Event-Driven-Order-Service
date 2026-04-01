package com.rahulagarwal.orderservice.domain.model.shared;

import java.util.UUID;

public class UserId {
    private final UUID id;

    public UserId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
