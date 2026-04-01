package com.rahulagarwal.orderservice.application.port;

import com.rahulagarwal.orderservice.domain.model.order.Order;

public interface OrderRepositoryPort {

    void save(Order order);

}
