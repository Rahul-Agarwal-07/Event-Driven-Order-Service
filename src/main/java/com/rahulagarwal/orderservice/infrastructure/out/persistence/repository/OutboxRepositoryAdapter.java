package com.rahulagarwal.orderservice.infrastructure.out.persistence.repository;

import com.rahulagarwal.orderservice.application.port.OutboxRepositoryPort;
import com.rahulagarwal.orderservice.domain.model.outbox.EventId;
import com.rahulagarwal.orderservice.domain.model.outbox.OutboxEvent;
import com.rahulagarwal.orderservice.domain.model.outbox.OutboxStatus;
import com.rahulagarwal.orderservice.infrastructure.out.persistence.entity.OutboxEventEntity;
import com.rahulagarwal.orderservice.infrastructure.out.persistence.mapper.OutboxMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public class OutboxRepositoryAdapter implements OutboxRepositoryPort {

    private static final int MAX_RETRIES = 3;
    private static final long BASE_DELAY = 5000;

    @PersistenceContext
    private final EntityManager entityManager;

    public OutboxRepositoryAdapter(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(OutboxEvent event) {

        OutboxEventEntity entity = OutboxMapper.toEntity(event);
        entityManager.persist(entity);
    }

    @Override
    @Transactional
    public List<OutboxEvent> fetchAndMarkProcessing(int batchSize, String workerId) {

        List<OutboxEventEntity> entities = entityManager
                .createNativeQuery("""
                      SELECT * FROM outbox_events
                      WHERE status IN('PENDING', 'RETRYABLE')
                      AND next_retry_at <= CURRENT_TIMESTAMP
                      ORDER BY occured_at
                      LIMIT :batchSize
                      FOR UPDATE SKIP LOCKED
                    """, OutboxEventEntity.class)
                .setParameter("batchSize", batchSize)
                .getResultList();

        entities.forEach(e -> {
            e.setStatus(OutboxStatus.PROCESSING);
            e.setProcessedAt(Instant.now());
            e.setProcessedBy(workerId);
        });

        return entities.stream()
                .map(OutboxMapper::toDomain)
                .toList();

    }



    @Override
    @Transactional
    public void markAsPublished(EventId eventId) {

        entityManager
                .createQuery("""
                        UPDATE OutboxEventEntity e
                        SET e.status = :status, e.processedAt = CURRENT_TIMESTAMP
                        WHERE e.eventId = :eventId
                    """
                )
                .setParameter("eventId", eventId.id())
                .setParameter("status", OutboxStatus.PUBLISHED)
                .executeUpdate();
    }

    @Override
    @Transactional
    public void markAsFailed(EventId eventId, String error) {
        OutboxEventEntity event = entityManager.find(OutboxEventEntity.class, eventId.id());

        int retryCount = event.getRetryCount();

        if(retryCount + 1 >= MAX_RETRIES)
            event.setStatus(OutboxStatus.DEAD);
        else
        {
            event.setStatus(OutboxStatus.RETRYABLE);
            event.setNextRetryAt(calculateNextRetryTime(retryCount + 1));
        }

        event.setErrorMessage(error);
        event.setRetryCount(retryCount + 1);
    }

    private Instant calculateNextRetryTime(int retryCount)
    {
        long delay = (long) (BASE_DELAY * Math.pow(2, retryCount));
        return Instant.now().plusMillis(delay);
    }
}
