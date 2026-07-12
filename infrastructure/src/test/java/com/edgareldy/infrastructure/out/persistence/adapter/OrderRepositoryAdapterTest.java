package com.edgareldy.infrastructure.out.persistence.adapter;

import com.edgareldy.domain.model.order.Order;
import com.edgareldy.domain.model.order.OrderLine;
import com.edgareldy.domain.model.order.OrderStatus;
import com.edgareldy.domain.model.shared.Money;
import com.edgareldy.domain.model.shared.PageResult;
import com.edgareldy.domain.port.in.command.PageQuery;
import com.edgareldy.infrastructure.PostgresTestcontainersConfiguration;
import com.edgareldy.infrastructure.out.persistence.repository.OrderJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * {@code @DataJpaTest} for {@link OrderRepositoryAdapter}. Specifically proves two non-obvious
 * behaviors by exercising them for real, not by reasoning about the mapping code: the
 * {@code @EntityGraph} on {@code OrderJpaRepository} actually avoids
 * {@code LazyInitializationException} when reading {@code lines} back, and updating an
 * already-saved order's status does not touch, duplicate, or drop its lines.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
@DataJpaTest
@Import(PostgresTestcontainersConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderRepositoryAdapterTest {

    private static final Currency EUR = Currency.getInstance("EUR");

    @Autowired
    private OrderJpaRepository orderJpaRepository;

    private OrderRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new OrderRepositoryAdapter(orderJpaRepository);
    }

    @Test
    void saves_a_new_order_and_assigns_an_id() {
        Order order = Order.create(1L, List.of(lineOf(9.99, 2)));

        Order saved = adapter.save(order);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getLines()).hasSize(1);
    }

    @Test
    void reads_lines_back_without_a_lazy_initialization_exception() {
        Order saved = adapter.save(Order.create(1L, List.of(lineOf(9.99, 2), lineOf(5, 1))));

        Optional<Order> found = adapter.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getLines()).hasSize(2);
    }

    @Test
    void updating_status_after_place_does_not_touch_the_lines_or_the_total() {
        Order saved = adapter.save(Order.create(1L, List.of(lineOf(9.99, 2))));
        saved.place();

        Order updated = adapter.save(saved);

        assertThat(updated.getStatus()).isEqualTo(OrderStatus.PLACED);
        assertThat(updated.getLines()).hasSize(1);
        assertThat(updated.getTotal()).isEqualTo(saved.getTotal());
        Optional<Order> reloaded = adapter.findById(saved.getId());
        assertThat(reloaded).isPresent();
        assertThat(reloaded.get().getLines()).hasSize(1);
        assertThat(reloaded.get().getTotal()).isEqualTo(saved.getTotal());
    }

    @Test
    void lists_orders_with_lines_loaded_paginated() {
        adapter.save(Order.create(1L, List.of(lineOf(9.99, 2))));
        adapter.save(Order.create(2L, List.of(lineOf(5, 1))));

        PageResult<Order> page = adapter.findAll(new PageQuery(0, 10));

        assertThat(page.content()).hasSize(2);
        assertThat(page.content()).allSatisfy(order -> assertThat(order.getLines()).isNotEmpty());
    }

    private static OrderLine lineOf(double unitPrice, int quantity) {
        return OrderLine.of(1L, "Product", quantity, new Money(BigDecimal.valueOf(unitPrice), EUR));
    }
}
