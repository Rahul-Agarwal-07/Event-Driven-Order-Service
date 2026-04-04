package com.rahulagarwal.orderservice.domain.model.shared;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class WorkerIdentity {

    private final String workerId = UUID.randomUUID().toString();

    public String getWorkerId() {
        return workerId;
    }
}