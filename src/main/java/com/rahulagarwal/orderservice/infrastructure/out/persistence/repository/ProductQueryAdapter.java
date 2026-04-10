package com.rahulagarwal.orderservice.infrastructure.out.persistence.repository;

import com.rahulagarwal.orderservice.application.port.ProductQueryPort;
import com.rahulagarwal.orderservice.domain.model.shared.Money;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class ProductQueryAdapter implements ProductQueryPort {
    @Override
    public Money getPriceByProductId(UUID productId) {
        return Money.of(
                BigDecimal.valueOf(100),
                "INR"
        );
    }
}
