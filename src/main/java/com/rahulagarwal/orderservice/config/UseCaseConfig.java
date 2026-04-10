package com.rahulagarwal.orderservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rahulagarwal.orderservice.application.port.OrderRepositoryPort;
import com.rahulagarwal.orderservice.application.port.OutboxRepositoryPort;
import com.rahulagarwal.orderservice.application.port.ProcessedEventRepositoryPort;
import com.rahulagarwal.orderservice.application.port.ProductQueryPort;
import com.rahulagarwal.orderservice.application.usecase.ConsumeEventUseCase;
import com.rahulagarwal.orderservice.application.usecase.PlaceOrderUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    ConsumeEventUseCase consumeEventUseCase(ProcessedEventRepositoryPort processedEventRepository)
    {
        return new ConsumeEventUseCase(processedEventRepository);
    }

    @Bean
    PlaceOrderUseCase placeOrderUseCase(
            OutboxRepositoryPort outboxRepository,
            ProductQueryPort productQuery,
            OrderRepositoryPort orderRepository,
            ObjectMapper objectMapper
    )
    {
        return new PlaceOrderUseCase(
                orderRepository,
                productQuery,
                outboxRepository,
                objectMapper
        );
    }

}
