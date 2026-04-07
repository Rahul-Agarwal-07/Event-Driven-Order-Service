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
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class KafkaConsumerIntegrationTest {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private ProcessedEventRepositoryPort processedEventRepository;

    @Test
    void should_consume_and_process_event() throws Exception
    {
        EventEnvelope eventEnvelope = new EventEnvelope(
                new EventId(UUID.randomUUID()),
                new AggregateId(UUID.randomUUID()),
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
        );

        Thread.sleep(3000);

        boolean exists = processedEventRepository.existsByEventId(eventEnvelope.getEventId());
        assertTrue(exists);
    }


}
