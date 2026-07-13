package com.edgareldy.application.service;

import com.edgareldy.domain.model.order.Order;
import com.edgareldy.domain.model.shared.PageResult;
import com.edgareldy.domain.port.in.ListOrdersUseCase;
import com.edgareldy.domain.port.in.command.PageQuery;
import com.edgareldy.domain.port.out.OrderRepositoryPort;

import java.util.Objects;

/**
 * Implements {@link ListOrdersUseCase}: pure orchestration, no business rule.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public final class ListOrdersService implements ListOrdersUseCase {

    private final OrderRepositoryPort orderRepositoryPort;

    public ListOrdersService(OrderRepositoryPort orderRepositoryPort) {
        this.orderRepositoryPort = Objects.requireNonNull(orderRepositoryPort, "orderRepositoryPort must not be null");
    }

    @Override
    public PageResult<Order> listOrders(PageQuery query) {
        return orderRepositoryPort.findAll(query);
    }
}
