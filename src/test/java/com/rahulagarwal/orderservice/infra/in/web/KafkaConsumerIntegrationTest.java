package com.rahulagarwal.orderservice.infra.in.web;

import com.rahulagarwal.orderservice.application.port.ProcessedEventRepositoryPort;
import com.rahulagarwal.orderservice.domain.model.outbox.AggregateId;
import com.rahulagarwal.orderservice.domain.model.outbox.EventId;
import com.rahulagarwal.orderservice.domain.model.shared.EventEnvelope;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.TestPropertySource;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertTrue;

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


}
