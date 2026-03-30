package com.rahulagarwal.orderservice.application.port;

import com.rahulagarwal.orderservice.domain.model.Order;

public interface OrderRepositoryPort {

    void save(Order order);

}
