package com.rahulagarwal.orderservice.infrastructure.in.web.dto;

import com.rahulagarwal.orderservice.application.dto.OrderDetails;
import com.rahulagarwal.orderservice.domain.model.shared.UserId;

import java.util.List;
import java.util.UUID;

public record CreateOrderRequestDTO(
        UUID userId,
        List<OrderItemRequestDTO> items
) {
}
