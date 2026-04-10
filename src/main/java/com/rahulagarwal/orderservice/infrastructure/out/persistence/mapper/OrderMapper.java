package com.rahulagarwal.orderservice.infrastructure.out.persistence.mapper;

import com.rahulagarwal.orderservice.domain.model.order.Order;
import com.rahulagarwal.orderservice.domain.model.shared.UserId;
import com.rahulagarwal.orderservice.infrastructure.out.persistence.entity.OrderEntity;
import com.rahulagarwal.orderservice.infrastructure.out.persistence.entity.OrderItemEntity;

import java.util.List;
import com.rahulagarwal.orderservice.domain.model.order.OrderItem;
import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderEntity toEntity(Order order) {

        OrderEntity entity = new OrderEntity(
                order.getOrderId().getId(),
                order.getUserId().getId(),
                null,
                order.getOrderStatus(),
                order.getPaymentStatus(),
                order.getTotalAmount().getAmount(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );

        List<OrderItemEntity> itemEntities = order.getOrderItems()
                .stream()
                .map(item -> OrderItemMapper.toEntity(item, entity))
                .collect(Collectors.toList());

        entity.setOrderItems(itemEntities);

        return entity;
    }

    public static Order toDomain(OrderEntity entity) {

        List<OrderItem> items = entity.getOrderItems()
                .stream()
                .map(OrderItemMapper::toDomain)
                .toList();

        return Order.create(
                new UserId(entity.getUserId()),
                items
        );
    }
}