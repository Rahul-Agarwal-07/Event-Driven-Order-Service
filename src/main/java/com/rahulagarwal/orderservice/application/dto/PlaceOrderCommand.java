package com.rahulagarwal.orderservice.application.dto;

import com.rahulagarwal.orderservice.domain.model.UserId;

import java.util.List;

public record PlaceOrderCommand(
        UserId userId,
        List<OrderDetails> items
) {
}
