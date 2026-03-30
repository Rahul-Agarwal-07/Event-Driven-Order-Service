package com.rahulagarwal.orderservice.application.dto;

import com.rahulagarwal.orderservice.domain.model.OrderId;

public record PlaceOrderResult(
        OrderId orderId
) {
}
