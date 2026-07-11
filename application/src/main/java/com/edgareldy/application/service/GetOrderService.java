package com.edgareldy.application.service;

import com.edgareldy.domain.model.order.Order;
import com.edgareldy.domain.port.in.GetOrderUseCase;
import com.edgareldy.domain.port.out.OrderRepositoryPort;

import java.util.Objects;
import java.util.Optional;

/**
 * Implements {@link GetOrderUseCase}: pure orchestration, no business rule.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public final class GetOrderService implements GetOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;

    public GetOrderService(OrderRepositoryPort orderRepositoryPort) {
        this.orderRepositoryPort = Objects.requireNonNull(orderRepositoryPort, "orderRepositoryPort must not be null");
    }

    @Override
    public Optional<Order> getOrder(Long orderId) {
        return orderRepositoryPort.findById(orderId);
    }
}
