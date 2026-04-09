package com.rahulagarwal.orderservice.infra.in.web;

import com.rahulagarwal.orderservice.application.port.ConsumeEventUseCasePort;
import com.rahulagarwal.orderservice.application.port.ProcessedEventRepositoryPort;
import com.rahulagarwal.orderservice.domain.model.consumer.ProcessedEvent;
import com.rahulagarwal.orderservice.domain.model.outbox.AggregateId;
import com.rahulagarwal.orderservice.domain.model.outbox.EventId;
import com.rahulagarwal.orderservice.domain.model.shared.EventEnvelope;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@EmbeddedKafka(
        partitions = 1,
        topics = {"order-events", "order-events-dlq"}
)
@TestPropertySource(properties = {
        "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",

        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",

        "spring.kafka.consumer.key-deserializer=org.springframework.kafka.support.serializer.ErrorHandlingDeserializer",
        "spring.kafka.consumer.value-deserializer=org.springframework.kafka.support.serializer.ErrorHandlingDeserializer",

        "spring.kafka.consumer.properties.spring.deserializer.key.delegate.class=org.apache.kafka.common.serialization.StringDeserializer",
        "spring.kafka.consumer.properties.spring.deserializer.value.delegate.class=org.springframework.kafka.support.serializer.JsonDeserializer",

        "spring.kafka.consumer.properties.spring.json.trusted.packages=*",
        "spring.kafka.consumer.properties.spring.json.value.default.type=com.rahulagarwal.orderservice.domain.model.shared.EventEnvelope",

        "spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer",
        "spring.kafka.consumer.auto-offset-reset=earliest"
})
class KafkaConsumerIntegrationTest {

    @Autowired
    private KafkaTemplate<String, EventEnvelope> kafkaTemplate;

    @Autowired
    private ProcessedEventRepositoryPort processedEventRepository;

    @Autowired
    private ConsumerFactory<String, EventEnvelope> consumerFactory;

    @MockitoSpyBean
    private ConsumeEventUseCasePort consumeEventUseCase;

    @Test
    void should_consume_and_process_event() throws Exception
    {
        EventEnvelope eventEnvelope = new EventEnvelope(
                new EventId(UUID.randomUUID()),
                new AggregateId(UUID.randomUUID().toString()),
                "ORDER",
                "ORDER_CREATED",
                "event-payload",
                Instant.now(),
                Instant.now()
        );

        kafkaTemplate.send(
                "order-events",
                eventEnvelope.getAggregateId().getValue(),
                eventEnvelope
        ).get();

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                assertTrue(processedEventRepository
                        .existsByEventId(eventEnvelope.getEventId()));
            });
    }

    @Test
    void should_send_event_to_dlq_when_processing_fails()
    {
        EventEnvelope event = new EventEnvelope(
                new EventId(UUID.randomUUID()),
                new AggregateId(UUID.randomUUID().toString()),
                "ORDER",
                "FAIL",
                "event-payload",
                Instant.now(),
                Instant.now()
        );

        kafkaTemplate.send("order-events", event);

        await()
            .atMost(10, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                assertFalse(processedEventRepository.existsByEventId(event.getEventId()));
            }
        );

        Consumer<String, EventEnvelope> consumer =
                consumerFactory.createConsumer("test-group", "test-client");

        consumer.subscribe(Collections.singletonList("order-events-dlq"));

        ConsumerRecords<String, EventEnvelope> records =
                KafkaTestUtils.getRecords(consumer, Duration.ofSeconds(10));

        boolean found = false;

        for(ConsumerRecord<String, EventEnvelope> record : records)
        {
            if(record.value().getEventId().equals(event.getEventId()))
            {
                found = true;
            }
        }

        assertTrue(found);
    }

    @Test
    void should_not_process_duplicate_event_again() throws Exception
    {
        EventEnvelope eventEnvelope = new EventEnvelope(
                new EventId(UUID.randomUUID()),
                new AggregateId(UUID.randomUUID().toString()),
                "ORDER",
                "ORDER_CREATED",
                "event-payload",
                Instant.now(),
                Instant.now()
        );

        // first time

        kafkaTemplate.send(
                "order-events",
                eventEnvelope.getAggregateId().getValue(),
                eventEnvelope
        ).get();

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                assertTrue(processedEventRepository
                        .existsByEventId(eventEnvelope.getEventId()));
            });

        // Second time
        kafkaTemplate.send(
                "order-events",
                eventEnvelope.getAggregateId().getValue(),
                eventEnvelope
        ).get();

        // Checks event is processed only once
        await()
                .atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    assertTrue(processedEventRepository
                            .existsByEventId(eventEnvelope.getEventId()));
                });
    }

    @Test
    void should_process_event_after_retrying_once() throws Exception
    {
        EventId eventId = new EventId(UUID.randomUUID());
        AggregateId aggregateId = new AggregateId(UUID.randomUUID().toString());

        EventEnvelope event = new EventEnvelope(
                eventId,
                aggregateId,
                "ORDER",
                "ORDER_CREATED",
                "event-payload",
                Instant.now(),
                Instant.now()
        );

        doThrow(new RuntimeException("Fail Once"))
                .doNothing()
                .when(consumeEventUseCase)
                .execute(any());


        kafkaTemplate.send(
                "order-events",
                event.getAggregateId().getValue(),
                event
        ).get();


        await()
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() ->  {
                    verify(consumeEventUseCase, atLeast(2)).execute(any());
                });
    }
}
