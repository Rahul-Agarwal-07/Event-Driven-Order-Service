package com.rahulagarwal.orderservice.application.usecase;

import com.rahulagarwal.orderservice.application.port.EventPublisherPort;
import com.rahulagarwal.orderservice.application.port.OutboxRepositoryPort;
import com.rahulagarwal.orderservice.application.port.PublishEventUseCasePort;
import com.rahulagarwal.orderservice.domain.model.outbox.AggregateId;
import com.rahulagarwal.orderservice.domain.model.outbox.EventId;
import com.rahulagarwal.orderservice.domain.model.outbox.OutboxEvent;
import com.rahulagarwal.orderservice.domain.model.outbox.OutboxStatus;
import com.rahulagarwal.orderservice.domain.model.shared.EventEnvelope;
import com.rahulagarwal.orderservice.domain.model.shared.WorkerIdentity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PublishEventUseCaseTest {

    private OutboxRepositoryPort outboxRepository;
    private EventPublisherPort eventPublisher;
    private WorkerIdentity workerIdentity;
    private PublishEventUseCasePort publishEventUseCase;

    @BeforeEach
    void setup()
    {
        outboxRepository = mock(OutboxRepositoryPort.class);
        eventPublisher = mock(EventPublisherPort.class);
        workerIdentity = mock(WorkerIdentity.class);
        publishEventUseCase = new PublishEventUseCase(outboxRepository,eventPublisher,workerIdentity);
    }

    @Test
    void should_publish_and_mark_events_published_successfully()
    {
        List<OutboxEvent> pendingEvents = new ArrayList<>();

        pendingEvents.add(
                new OutboxEvent(
                        new EventId(UUID.randomUUID()),
                        new AggregateId(UUID.randomUUID()),
                        "ORDER",
                        "ORDER_CREATED",
                        "event-payload",
                        OutboxStatus.PENDING,
                        Instant.now(),
                        Instant.now(),
                        Instant.now(),
                        0,
                        null
                )
        );

        pendingEvents.add(
                new OutboxEvent(
                        new EventId(UUID.randomUUID()),
                        new AggregateId(UUID.randomUUID()),
                        "ORDER",
                        "ORDER_CREATED",
                        "event-payload",
                        OutboxStatus.PENDING,
                        Instant.now(),
                        Instant.now(),
                        Instant.now(),
                        0,
                        null
                )
        );

        when(workerIdentity.getWorkerId())
                .thenReturn("worker-id1");

        when(outboxRepository.fetchAndMarkProcessing(anyInt(), anyString()))
                .thenReturn(pendingEvents);

        publishEventUseCase.execute();

        ArgumentCaptor<EventEnvelope> captor = ArgumentCaptor.forClass(EventEnvelope.class);

        verify(eventPublisher, times(2)).publish(captor.capture());
        verify(outboxRepository, times(2)).markAsPublished(any(EventId.class));

        List<EventEnvelope> publishedEvents = captor.getAllValues();
        assertEquals(2, publishedEvents.size());
    }

    @Test
    void should_mark_event_failed_successfully()
    {
        OutboxEvent event = new OutboxEvent(
                new EventId(UUID.randomUUID()),
                new AggregateId(UUID.randomUUID()),
                "ORDER",
                "ORDER_CREATED",
                "event-payload",
                OutboxStatus.PENDING,
                Instant.now(),
                Instant.now(),
                Instant.now(),
                0,
                null
        );

        when(workerIdentity.getWorkerId())
                .thenReturn("worker-id1");

        when(outboxRepository.fetchAndMarkProcessing(anyInt(), anyString()))
                .thenReturn(List.of(event));

        doThrow(new RuntimeException("Kakfa Down"))
                .when(eventPublisher).publish(any(EventEnvelope.class));

        publishEventUseCase.execute();

        verify(eventPublisher).publish(any(EventEnvelope.class));
        verify(outboxRepository).markAsFailed(eq(event.getEventId()), anyString());
    }
}
