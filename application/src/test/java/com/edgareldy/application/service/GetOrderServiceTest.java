package com.edgareldy.application.service;

import com.edgareldy.domain.model.order.Order;
import com.edgareldy.domain.model.order.OrderLine;
import com.edgareldy.domain.model.order.OrderStatus;
import com.edgareldy.domain.model.shared.Money;
import com.edgareldy.domain.port.out.OrderRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Mockito tests for {@link GetOrderService}: delegates to
 * {@link OrderRepositoryPort#findById}, propagating an empty {@link Optional} as-is.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
@ExtendWith(MockitoExtension.class)
class GetOrderServiceTest {

    @Mock
    private OrderRepositoryPort orderRepositoryPort;

    @Test
    void returns_the_order_when_found() {
        Money unitPrice = new Money(BigDecimal.valueOf(9.99), Currency.getInstance("EUR"));
        OrderLine line = OrderLine.of(1L, "Mechanical keyboard", 1, unitPrice);
        Order order = Order.reconstitute(99L, 7L, List.of(line), OrderStatus.PLACED, Instant.now());
        when(orderRepositoryPort.findById(99L)).thenReturn(Optional.of(order));
        GetOrderService service = new GetOrderService(orderRepositoryPort);

        Optional<Order> result = service.getOrder(99L);

        assertThat(result).contains(order);
    }

    @Test
    void returns_empty_when_not_found() {
        when(orderRepositoryPort.findById(404L)).thenReturn(Optional.empty());
        GetOrderService service = new GetOrderService(orderRepositoryPort);

        Optional<Order> result = service.getOrder(404L);

        assertThat(result).isEmpty();
    }
}
