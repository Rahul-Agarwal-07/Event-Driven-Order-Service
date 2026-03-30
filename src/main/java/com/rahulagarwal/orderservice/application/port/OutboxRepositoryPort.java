package com.rahulagarwal.orderservice.application.port;

public interface OutboxRepositoryPort {

    void save(OutboxEvent event);

}
