package com.rahulagarwal.orderservice.infrastructure.out.persistence.mapper;

import com.rahulagarwal.orderservice.domain.model.order.OrderItem;
import com.rahulagarwal.orderservice.domain.model.shared.Money;
import com.rahulagarwal.orderservice.infrastructure.out.persistence.entity.OrderEntity;
import com.rahulagarwal.orderservice.infrastructure.out.persistence.entity.OrderItemEntity;

import java.util.UUID;

public class OrderItemMapper {

    public static OrderItemEntity toEntity(OrderItem item, OrderEntity orderEntity) {

        return new OrderItemEntity(
                UUID.randomUUID(),
                item.getProductId(),
                item.getPrice().getAmount(),
                item.getQuantity(),
                orderEntity
        );
    }

    public static OrderItem toDomain(OrderItemEntity entity) {

        return new OrderItem(
                entity.getProductId(),
                entity.getQuantity(),
                Money.of(entity.getPrice(), "INR")
        );
    }
}