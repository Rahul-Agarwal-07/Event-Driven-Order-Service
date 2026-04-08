package com.rahulagarwal.orderservice.infrastructure.out.persistence.repository;
import com.rahulagarwal.orderservice.infrastructure.out.persistence.entity.ProcessedEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaProcessedEventRepository extends JpaRepository<ProcessedEventEntity, UUID> {

    boolean existsByEventId(UUID eventId);

}