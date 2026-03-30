package com.rahulagarwal.orderservice.application.port;

import com.rahulagarwal.orderservice.application.dto.PlaceOrderCommand;
import com.rahulagarwal.orderservice.application.dto.PlaceOrderResult;

import java.util.UUID;

public interface PlaceOrderUseCasePort {

    PlaceOrderResult execute(PlaceOrderCommand command);

}
