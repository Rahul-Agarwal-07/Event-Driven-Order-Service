package com.rahulagarwal.orderservice.infrastructure.in.web.dto;

import java.util.UUID;

public record OrderItemRequestDTO(
        UUID productId,
        int quantity
) {}