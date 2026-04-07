package com.rahulagarwal.orderservice.config;

import com.rahulagarwal.orderservice.application.port.ProcessedEventRepositoryPort;
import com.rahulagarwal.orderservice.application.usecase.ConsumeEventUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    ConsumeEventUseCase consumeEventUseCase(ProcessedEventRepositoryPort processedEventRepository)
    {
        return new ConsumeEventUseCase(processedEventRepository);
    }

}
