package com.rahulagarwal.orderservice.infrastructure.out.persistence.repository;

import com.rahulagarwal.orderservice.application.port.ProcessedEventRepositoryPort;
import com.rahulagarwal.orderservice.domain.model.consumer.ProcessedEvent;
import com.rahulagarwal.orderservice.domain.model.outbox.EventId;
import com.rahulagarwal.orderservice.infrastructure.out.persistence.entity.ProcessedEventEntity;
import org.springframework.stereotype.Repository;

@Repository
public class ProcessedEventRepositoryAdapter implements ProcessedEventRepositoryPort {

    private final JpaProcessedEventRepository jpaProcessedEventRepository;

    public ProcessedEventRepositoryAdapter(JpaProcessedEventRepository jpaProcessedEventRepository) {
        this.jpaProcessedEventRepository = jpaProcessedEventRepository;
    }

    @Override
    public boolean existsByEventId(EventId eventId) {
        return jpaProcessedEventRepository.existsByEventId(eventId.id());
    }

    @Override
    public void save(ProcessedEvent event) {
        jpaProcessedEventRepository.save(
                new ProcessedEventEntity(
                        event.getEventId().id(),
                        event.getProcessedAt()
                )
        );
    }
}
