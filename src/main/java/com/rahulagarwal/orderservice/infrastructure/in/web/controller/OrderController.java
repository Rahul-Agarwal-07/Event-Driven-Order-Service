package com.rahulagarwal.orderservice.infrastructure.in.web.controller;

import com.rahulagarwal.orderservice.application.dto.OrderDetails;
import com.rahulagarwal.orderservice.application.dto.PlaceOrderCommand;
import com.rahulagarwal.orderservice.application.dto.PlaceOrderResult;
import com.rahulagarwal.orderservice.application.port.PlaceOrderUseCasePort;
import com.rahulagarwal.orderservice.application.usecase.PlaceOrderUseCase;
import com.rahulagarwal.orderservice.domain.model.shared.UserId;
import com.rahulagarwal.orderservice.infrastructure.in.web.dto.CreateOrderRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final PlaceOrderUseCasePort placeOrderUseCase;

    public OrderController(PlaceOrderUseCase placeOrderUseCase) {
        this.placeOrderUseCase = placeOrderUseCase;
    }

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody CreateOrderRequestDTO payload)
    {

        List<OrderDetails> items = payload.items().stream()
                .map(e -> new OrderDetails(
                        e.productId(),
                        e.quantity()
                ))
                .toList();

        PlaceOrderCommand command = new PlaceOrderCommand(
                new UserId(payload.userId()),
                items
        );

        PlaceOrderResult result = placeOrderUseCase.execute(command);

        return ResponseEntity.ok("Order Created with ID : " + result.orderId().getId());
    }
}
