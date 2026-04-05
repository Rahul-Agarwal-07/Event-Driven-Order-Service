package com.rahulagarwal.orderservice.application.usecase;

import com.rahulagarwal.orderservice.application.port.EventPublisherPort;
import com.rahulagarwal.orderservice.application.port.OutboxRepositoryPort;
import com.rahulagarwal.orderservice.application.port.PublishEventUseCasePort;
import com.rahulagarwal.orderservice.domain.model.outbox.OutboxEvent;
import com.rahulagarwal.orderservice.domain.model.shared.EventEnvelope;
import com.rahulagarwal.orderservice.domain.model.shared.WorkerIdentity;
import org.hibernate.type.SerializationException;

import java.util.List;

public class PublishEventUseCase implements PublishEventUseCasePort {

    private static final int BATCH_SIZE = 50;
    private final OutboxRepositoryPort outboxRepository;
    private final EventPublisherPort eventPublisher;
    private final WorkerIdentity workerIdentity;

    public PublishEventUseCase(
            OutboxRepositoryPort outboxRepository,
            EventPublisherPort eventPublisher,
            WorkerIdentity workerIdentity
    ) {
        this.outboxRepository = outboxRepository;
        this.eventPublisher = eventPublisher;
        this.workerIdentity = workerIdentity;
    }

    @Override
    public void execute() {

        List<OutboxEvent> pendingEvents = outboxRepository.fetchAndMarkProcessing(BATCH_SIZE, workerIdentity.getWorkerId());

        for(OutboxEvent event : pendingEvents)
        {
            try {
                EventEnvelope envelope = toEventEnvelope(event);
                eventPublisher.publish(envelope);
                outboxRepository.markAsPublished(event.getEventId());

            } catch (SerializationException e) {
                outboxRepository.markAsFailed(event.getEventId(), e.getMessage());

            } catch (Exception e) {
                outboxRepository.markAsFailed(event.getEventId(), e.getMessage());
            }
        }
    }

    private EventEnvelope toEventEnvelope(OutboxEvent event)
    {
        return new EventEnvelope(
                event.getEventId(),
                event.getAggregateId(),
                event.getAggregateType(),
                event.getEventType(),
                event.getPayload(),
                event.getOccurredAt(),
                event.getCreatedAt()
        );
    }
}
