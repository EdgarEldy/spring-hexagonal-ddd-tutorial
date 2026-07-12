package com.edgareldy.infrastructure.in.web.mapper;

import com.edgareldy.domain.model.order.Order;
import com.edgareldy.domain.model.order.OrderLine;
import com.edgareldy.domain.model.shared.Money;
import com.edgareldy.domain.port.in.command.CreateOrderCommand;
import com.edgareldy.domain.port.in.command.CreateOrderLineCommand;
import com.edgareldy.infrastructure.in.web.dto.common.MoneyDto;
import com.edgareldy.infrastructure.in.web.dto.order.OrderLineRequest;
import com.edgareldy.infrastructure.in.web.dto.order.OrderLineResponse;
import com.edgareldy.infrastructure.in.web.dto.order.OrderRequest;
import com.edgareldy.infrastructure.in.web.dto.order.OrderResponse;

/**
 * Manual mapper between the order web DTOs and the domain. {@code subtotal} in
 * {@link OrderLineResponse} is read from {@code OrderLine.getSubtotal()}, never recomputed
 * here: the derivation stays owned by the domain.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public final class OrderWebMapper {

    private OrderWebMapper() {
    }

    public static CreateOrderCommand toCommand(OrderRequest request) {
        return new CreateOrderCommand(request.customerId(), request.lines().stream().map(OrderWebMapper::toLineCommand).toList());
    }

    private static CreateOrderLineCommand toLineCommand(OrderLineRequest lineRequest) {
        return new CreateOrderLineCommand(lineRequest.productId(), lineRequest.quantity());
    }

    public static OrderResponse toResponse(Order order) {
        return new OrderResponse(order.getId(), order.getCustomerId(), order.getStatus().name(),
                order.getLines().stream().map(OrderWebMapper::toLineResponse).toList(), toMoneyDto(order.getTotal()),
                order.getPlacedAt());
    }

    private static OrderLineResponse toLineResponse(OrderLine line) {
        return new OrderLineResponse(line.getProductId(), line.getProductName(), line.getQuantity(),
                toMoneyDto(line.getUnitPrice()), toMoneyDto(line.getSubtotal()));
    }

    private static MoneyDto toMoneyDto(Money money) {
        return new MoneyDto(money.amount(), money.currency().getCurrencyCode());
    }
}
