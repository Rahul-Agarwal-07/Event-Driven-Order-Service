package com.rahulagarwal.orderservice.domain.model;

import com.rahulagarwal.orderservice.domain.exception.CancellationNotAllowedException;
import com.rahulagarwal.orderservice.domain.exception.InvalidStateException;
import com.rahulagarwal.orderservice.domain.exception.OrderAlreadyProcessedException;
import com.rahulagarwal.orderservice.domain.exception.OrderItemNotFoundException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Order {

    private final OrderId orderId;
    private final UserId userId;
    private List<OrderItem> orderItems;
    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;
    private Money totalAmount;
    private final Instant createdAt;
    private Instant updatedAt;

    private Order(
            OrderId orderId,
            UserId userId,
            List<OrderItem> orderItems,
            PaymentStatus paymentStatus,
            OrderStatus orderStatus,
            Money totalAmount,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderItems = new ArrayList<>(orderItems);
        this.paymentStatus = paymentStatus;
        this.orderStatus = orderStatus;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // behavior methods

    public static Order create(
            UserId userId,
            List<OrderItem> orderItems
    )
    {
        OrderId orderId = new OrderId(UUID.randomUUID());

        if(userId == null)
            throw new InvalidStateException("User Id cannot be null");

        if(orderItems == null || orderItems.isEmpty())
            throw new InvalidStateException("Order items cannot be null or empty");

        return new Order(
                orderId,
                userId,
                orderItems,
                PaymentStatus.PENDING,
                OrderStatus.CREATED,
                calculateTotalAmount(orderItems),
                Instant.now(),
                null
        );
    }

    public static Order restore(
            OrderId orderId,
            UserId userId,
            List<OrderItem> orderItems,
            PaymentStatus paymentStatus,
            OrderStatus orderStatus,
            Money totalAmount,
            Instant createdAt,
            Instant updatedAt
    ) {
        return new Order(
                orderId,
                userId,
                orderItems,
                paymentStatus,
                orderStatus,
                totalAmount,
                createdAt,
                updatedAt
        );
    }

    public void markProcessing()
    {
        if(orderStatus != OrderStatus.CREATED)
            throw new OrderAlreadyProcessedException();

        orderStatus = OrderStatus.PROCESSING;
        updatedAt = Instant.now();
    }

    public void cancel()
    {
        if(orderStatus != OrderStatus.CREATED)
            throw new CancellationNotAllowedException();

        orderStatus = OrderStatus.CANCELLED;
        updatedAt = Instant.now();
    }

    public void complete()
    {
        if(orderStatus == OrderStatus.PROCESSING && paymentStatus == PaymentStatus.SUCCESS)
        {
            orderStatus = OrderStatus.COMPLETED;
            updatedAt = Instant.now();
            return;
        }

        throw new InvalidStateException("Order cannot be completed at current state");
    }

    public void markPaymentSuccess()
    {
        if(paymentStatus == PaymentStatus.FAILED)
            throw new InvalidStateException("Payment Already Failed. Cannot Mark Success");

        paymentStatus = PaymentStatus.SUCCESS;
        updatedAt = Instant.now();
    }

    public void markPaymentFailed()
    {
        if(paymentStatus == PaymentStatus.SUCCESS)
            throw new InvalidStateException("Payment is Already Success. Cannot mark Failed");

        paymentStatus = PaymentStatus.FAILED;
        updatedAt = Instant.now();
    }

    public void addItem(OrderItem item)
    {
        if(orderStatus != OrderStatus.CREATED)
            throw new OrderAlreadyProcessedException("Cannot add items during/after processing");

        for (int i = 0; i < orderItems.size(); i++) {
            OrderItem existing = orderItems.get(i);

            if (existing.getProductId().equals(item.getProductId())) {
                orderItems.set(i,
                        existing.withQuantity(existing.getQuantity() + item.getQuantity())
                );
                totalAmount = calculateTotalAmount(orderItems);
                updatedAt = Instant.now();
                return;
            }
        }


        orderItems.add(item);
        updatedAt = Instant.now();
        totalAmount = calculateTotalAmount(orderItems);
    }

    public void removeItem(UUID productId)
    {
        if(orderItems.size() == 1)
            throw new InvalidStateException("Order must have at least 1 item");


        if(orderStatus != OrderStatus.CREATED)
            throw new OrderAlreadyProcessedException("Cannot remove items during/after processing");


        boolean removed = orderItems.removeIf(item -> item.getProductId().equals(productId));

        if(!removed) throw new OrderItemNotFoundException();

        updatedAt = Instant.now();
        totalAmount = calculateTotalAmount(orderItems);
    }

    public void updateItemQuantity(UUID productId, int qty)
    {
        if(orderStatus != OrderStatus.CREATED)
            throw new OrderAlreadyProcessedException("Cannot modify quantity during/after processing");

        if(qty <= 0) throw new InvalidStateException("Quantity must be greater than 0");

        List<OrderItem> newItems = new ArrayList<>(orderItems);

        for (int i = 0; i < newItems.size(); i++) {
            OrderItem item = newItems.get(i);

            if (item.getProductId().equals(productId)) {
                newItems.set(i, item.withQuantity(qty));
                this.orderItems = newItems;
                this.totalAmount = calculateTotalAmount(newItems);
                this.updatedAt = Instant.now();
                return;
            }
        }

        throw new OrderItemNotFoundException();
    }

    private static Money calculateTotalAmount(List<OrderItem> orderItems) {

        Money total = Money.zero("INR");

        for (OrderItem item : orderItems) {
            total = total.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        Money tax = total.multiply(BigDecimal.valueOf(0.18));
        Money otherCharges = tax.multiply(BigDecimal.valueOf(0.05));

        return total.add(tax).add(otherCharges);
    }

    // Getters

    public OrderId getOrderId() {
        return orderId;
    }

    public UserId getUserId() {
        return userId;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public Money getTotalAmount() {
        return totalAmount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
