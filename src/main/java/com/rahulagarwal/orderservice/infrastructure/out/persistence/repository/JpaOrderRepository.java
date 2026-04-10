package com.rahulagarwal.orderservice.infrastructure.out.persistence.repository;

import com.rahulagarwal.orderservice.infrastructure.out.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaOrderRepository extends JpaRepository<OrderEntity, UUID> {
}
