package com.rahulagarwal.orderservice.application.usecase;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rahulagarwal.orderservice.application.dto.OrderDetails;
import com.rahulagarwal.orderservice.application.dto.PlaceOrderCommand;
import com.rahulagarwal.orderservice.application.dto.PlaceOrderResult;
import com.rahulagarwal.orderservice.application.port.OrderRepositoryPort;
import com.rahulagarwal.orderservice.application.port.OutboxRepositoryPort;
import com.rahulagarwal.orderservice.application.port.PlaceOrderUseCasePort;
import com.rahulagarwal.orderservice.application.port.ProductQueryPort;
import com.rahulagarwal.orderservice.domain.exception.UnsupportedEventTypeException;
import com.rahulagarwal.orderservice.domain.model.order.OrderCreatedEvent;
import com.rahulagarwal.orderservice.domain.model.outbox.AggregateId;
import com.rahulagarwal.orderservice.domain.model.outbox.EventId;
import com.rahulagarwal.orderservice.domain.model.outbox.OutboxEvent;
import com.rahulagarwal.orderservice.domain.model.shared.DomainEvent;
import com.rahulagarwal.orderservice.domain.model.shared.Money;
import com.rahulagarwal.orderservice.domain.model.order.Order;
import com.rahulagarwal.orderservice.domain.model.order.OrderItem;
import jakarta.transaction.Transactional;

import java.time.Instant;
import java.util.*;

@Transactional
public class PlaceOrderUseCase implements PlaceOrderUseCasePort {

    private final OrderRepositoryPort orderRepository;
    private final ProductQueryPort productQuery;
    private final OutboxRepositoryPort outboxRepository;
    private final ObjectMapper objectMapper;

    public PlaceOrderUseCase(OrderRepositoryPort orderRepository, ProductQueryPort productQuery, OutboxRepositoryPort outboxRepository, ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.productQuery = productQuery;
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public PlaceOrderResult execute(PlaceOrderCommand command)
    {
        List<OrderItem> itemList = new ArrayList<>();

        for(OrderDetails item : command.items())
        {
            Money price = productQuery.getPriceByProductId(item.getProductId());

            itemList.add(
                    new OrderItem(
                            item.getProductId(),
                            item.getQuantity(),
                            price
                    )
            );
        }

        Order order = Order.create(
                command.userId(),
                itemList
        );

        orderRepository.save(order);

        List<DomainEvent> events = order.pullEvents();

        for(DomainEvent event : events)
        {
            OutboxEvent outboxEvent = mapToOutbox(event);
            outboxRepository.save(outboxEvent);
        }

        return new PlaceOrderResult(
                order.getOrderId()
        );
    }

    private OutboxEvent mapToOutbox(DomainEvent event)
    {
        if(event instanceof OrderCreatedEvent e)
        {
            Instant now = Instant.now();

            return new OutboxEvent(
                    new EventId(UUID.randomUUID()),
                    new AggregateId(e.getOrderId()),
                    "ORDER",
                    event.getEventType(),
                    serialize(event),
                    event.getOccurredAt(),
                    now,
                    now,
                    0,
                    null
            );
        }

        throw new UnsupportedEventTypeException();
    }

    private String serialize(DomainEvent event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize event", e);
        }
    }
}
