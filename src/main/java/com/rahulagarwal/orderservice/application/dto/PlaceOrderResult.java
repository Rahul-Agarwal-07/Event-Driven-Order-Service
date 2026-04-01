package com.rahulagarwal.orderservice.application.dto;

import com.rahulagarwal.orderservice.domain.model.order.OrderId;

public record PlaceOrderResult(
        OrderId orderId
) {
}
