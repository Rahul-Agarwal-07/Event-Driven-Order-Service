package com.rahulagarwal.orderservice.application.usecase;
import com.rahulagarwal.orderservice.application.dto.OrderDetails;
import com.rahulagarwal.orderservice.application.dto.PlaceOrderCommand;
import com.rahulagarwal.orderservice.application.dto.PlaceOrderResult;
import com.rahulagarwal.orderservice.application.port.OrderRepositoryPort;
import com.rahulagarwal.orderservice.application.port.OutboxRepositoryPort;
import com.rahulagarwal.orderservice.application.port.PlaceOrderUseCasePort;
import com.rahulagarwal.orderservice.application.port.ProductQueryPort;
import com.rahulagarwal.orderservice.domain.model.Money;
import com.rahulagarwal.orderservice.domain.model.Order;
import com.rahulagarwal.orderservice.domain.model.OrderItem;
import jakarta.transaction.Transactional;
import java.util.*;

@Transactional
public class PlaceOrderUseCase implements PlaceOrderUseCasePort {

    private final OrderRepositoryPort orderRepository;
    private final ProductQueryPort productQuery;
    private final OutboxRepositoryPort outboxRepository;

    public PlaceOrderUseCase(OrderRepositoryPort orderRepository, ProductQueryPort productQuery, OutboxRepositoryPort outboxRepository) {
        this.orderRepository = orderRepository;
        this.productQuery = productQuery;
        this.outboxRepository = outboxRepository;
    }


    @Override
    public PlaceOrderResult execute(PlaceOrderCommand command) {

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

        OutboxEvent event = new OutboxEvent("ORDER_CREATED");

        orderRepository.save(order);
        outboxRepository.save(event);

        return new PlaceOrderResult(
                order.getOrderId()
        );
    }
}
