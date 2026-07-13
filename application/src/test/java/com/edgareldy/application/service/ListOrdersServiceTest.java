package com.edgareldy.application.service;

import com.edgareldy.domain.model.order.Order;
import com.edgareldy.domain.model.order.OrderLine;
import com.edgareldy.domain.model.order.OrderStatus;
import com.edgareldy.domain.model.shared.Money;
import com.edgareldy.domain.model.shared.PageResult;
import com.edgareldy.domain.port.in.command.PageQuery;
import com.edgareldy.domain.port.out.OrderRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Mockito tests for {@link ListOrdersService}: a thin pass-through to
 * {@link OrderRepositoryPort#findAll}.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
@ExtendWith(MockitoExtension.class)
class ListOrdersServiceTest {

    @Mock
    private OrderRepositoryPort orderRepositoryPort;

    @Test
    void delegates_to_the_repository_port() {
        PageQuery query = new PageQuery(0, 10);
        Money unitPrice = new Money(BigDecimal.valueOf(9.99), Currency.getInstance("EUR"));
        OrderLine line = OrderLine.of(1L, "Mechanical keyboard", 1, unitPrice);
        Order order = Order.reconstitute(99L, 7L, List.of(line), OrderStatus.PLACED, Instant.now());
        PageResult<Order> page = new PageResult<>(List.of(order), 0, 10, 1, 1);
        when(orderRepositoryPort.findAll(query)).thenReturn(page);
        ListOrdersService service = new ListOrdersService(orderRepositoryPort);

        PageResult<Order> result = service.listOrders(query);

        assertThat(result).isEqualTo(page);
    }
}
