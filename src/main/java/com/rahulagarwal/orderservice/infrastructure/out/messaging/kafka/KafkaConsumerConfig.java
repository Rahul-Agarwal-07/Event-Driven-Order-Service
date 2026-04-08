package com.rahulagarwal.orderservice.infrastructure.out.messaging.kafka;

import com.rahulagarwal.orderservice.application.port.ConsumeEventUseCasePort;
import com.rahulagarwal.orderservice.domain.model.shared.EventEnvelope;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public DeadLetterPublishingRecoverer recoverer(KafkaTemplate<String, EventEnvelope> kafkaTemplate) {
        return new DeadLetterPublishingRecoverer(
                kafkaTemplate,
                (record, ex) -> {
                    System.out.println("Sending to DLQ: " + record.value());
                    return new TopicPartition("order-events-dlq", record.partition());
                }
        );
    }

    @Bean
    public DefaultErrorHandler errorHandler(DeadLetterPublishingRecoverer recoverer) {
        FixedBackOff backOff = new FixedBackOff(2000L, 3); // 3 retries, 2 sec gap
        return new DefaultErrorHandler(recoverer, backOff);
    }

    @Bean
    public KafkaEventConsumer kafkaEventConsumer(ConsumeEventUseCasePort consumeEventUseCase)
    {
        return new KafkaEventConsumer(consumeEventUseCase);
    }

}
