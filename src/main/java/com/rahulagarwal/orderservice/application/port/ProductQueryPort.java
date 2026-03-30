package com.rahulagarwal.orderservice.application.port;

import com.rahulagarwal.orderservice.domain.model.Money;

import java.util.UUID;

public interface ProductQueryPort {

    Money getPriceByProductId(UUID productId);

}
