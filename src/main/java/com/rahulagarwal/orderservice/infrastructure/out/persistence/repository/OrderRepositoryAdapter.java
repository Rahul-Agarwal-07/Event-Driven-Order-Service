package com.rahulagarwal.orderservice.infrastructure.out.persistence.repository;

import com.rahulagarwal.orderservice.application.port.OrderRepositoryPort;
import com.rahulagarwal.orderservice.domain.model.order.Order;
import com.rahulagarwal.orderservice.infrastructure.out.persistence.entity.OrderEntity;
import com.rahulagarwal.orderservice.infrastructure.out.persistence.mapper.OrderMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class OrderRepositoryAdapter implements OrderRepositoryPort {

    private final JpaOrderRepository orderRepository;

    @PersistenceContext
    private final EntityManager entityManager;

    public OrderRepositoryAdapter(JpaOrderRepository orderRepository, EntityManager entityManager) {
        this.orderRepository = orderRepository;
        this.entityManager = entityManager;
    }

    @Override
    public void save(Order order) {
        OrderEntity entity = OrderMapper.toEntity(order);
        entityManager.persist(entity);
    }
}
