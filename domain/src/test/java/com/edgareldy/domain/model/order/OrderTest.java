package com.edgareldy.domain.model.order;

import com.edgareldy.domain.event.DomainEvent;
import com.edgareldy.domain.event.OrderPlacedEvent;
import com.edgareldy.domain.exception.EmptyOrderException;
import com.edgareldy.domain.model.shared.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for the {@link Order} Aggregate Root: the "not empty" invariant, the total always
 * recomputed from the lines, and {@link OrderPlacedEvent} being raised on {@code place()}.
 * <p>
 * Created by edgar.muhamyangabo on 7/11/26
 * Author : edgar.muhamyangabo
 * Date : 7/11/26
 * Project : spring-hexagonal-ddd-tutorial
 */
class OrderTest {

    private static final Currency EUR = Currency.getInstance("EUR");

    @Test
    void _01_ShouldRejectCreation_WhenOrderHasNoLine() {
        assertThatThrownBy(() -> Order.create(1L, List.of())).isInstanceOf(EmptyOrderException.class);
    }

    @Test
    void _02_ShouldRejectPlacement_WhenOrderHasNoLine() {
        Order order = Order.reconstitute(99L, 1L, List.of(), OrderStatus.DRAFT, null);

        assertThatThrownBy(order::place).isInstanceOf(EmptyOrderException.class);
    }

    @Test
    void _03_ShouldRejectPlacement_WhenOrderHasNotBeenSaved() {
        Order order = Order.create(1L, List.of(lineOf(9.99, 1)));

        assertThatThrownBy(order::place).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void _04_ShouldTransitionToPlacedAndStampPlacedAt_WhenOrderIsPlaced() {
        Order order = Order.reconstitute(99L, 1L, List.of(lineOf(9.99, 2)), OrderStatus.DRAFT, null);

        order.place();

        assertThat(order.getStatus()).isEqualTo(OrderStatus.PLACED);
        assertThat(order.getPlacedAt()).isNotNull();
    }

    @Test
    void _05_ShouldRaiseOrderPlacedEventWithRecomputedTotal_WhenOrderIsPlaced() {
        Order order = Order.reconstitute(99L, 7L, List.of(lineOf(10, 2), lineOf(5, 1)), OrderStatus.DRAFT, null);

        order.place();
        List<DomainEvent> events = order.pullDomainEvents();

        assertThat(events).hasSize(1);
        OrderPlacedEvent event = (OrderPlacedEvent) events.get(0);
        assertThat(event.customerId()).isEqualTo(7L);
        assertThat(event.total().amount()).isEqualByComparingTo(BigDecimal.valueOf(25));
    }

    @Test
    void _06_ShouldDrainEvents_WhenDomainEventsArePulled() {
        Order order = Order.reconstitute(99L, 1L, List.of(lineOf(9.99, 1)), OrderStatus.DRAFT, null);
        order.place();

        order.pullDomainEvents();
        List<DomainEvent> secondPull = order.pullDomainEvents();

        assertThat(secondPull).isEmpty();
    }

    @Test
    void _07_ShouldRejectPlacement_WhenOrderIsAlreadyPlaced() {
        Order order = Order.reconstitute(99L, 1L, List.of(lineOf(9.99, 1)), OrderStatus.DRAFT, null);
        order.place();

        assertThatThrownBy(order::place).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void _08_ShouldRecomputeTotalFromLines_WhenTotalIsRequested() {
        Order order = Order.create(1L, List.of(lineOf(10, 2), lineOf(5, 3)));

        assertThat(order.getTotal().amount()).isEqualByComparingTo(BigDecimal.valueOf(35));
    }

    @Test
    void _09_ShouldNotRaiseNewEvent_WhenPlacedOrderIsReconstituted() {
        Order order = Order.reconstitute(42L, 1L, List.of(lineOf(9.99, 1)), OrderStatus.PLACED, Instant.now());

        assertThat(order.pullDomainEvents()).isEmpty();
    }

    private static OrderLine lineOf(double unitPrice, int quantity) {
        return OrderLine.of(1L, "Product", quantity, new Money(BigDecimal.valueOf(unitPrice), EUR));
    }
}
