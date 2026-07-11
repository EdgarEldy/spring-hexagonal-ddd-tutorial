package com.edgareldy.application.service;

import com.edgareldy.domain.exception.CustomerNotFoundException;
import com.edgareldy.domain.exception.ProductNotFoundException;
import com.edgareldy.domain.model.customer.Customer;
import com.edgareldy.domain.model.order.Order;
import com.edgareldy.domain.model.order.OrderLine;
import com.edgareldy.domain.model.product.Product;
import com.edgareldy.domain.port.in.CreateOrderUseCase;
import com.edgareldy.domain.port.in.command.CreateOrderCommand;
import com.edgareldy.domain.port.in.command.CreateOrderLineCommand;
import com.edgareldy.domain.port.out.CustomerRepositoryPort;
import com.edgareldy.domain.port.out.DomainEventPublisherPort;
import com.edgareldy.domain.port.out.OrderRepositoryPort;
import com.edgareldy.domain.port.out.ProductRepositoryPort;

import java.util.List;
import java.util.Objects;

/**
 * Implements {@link CreateOrderUseCase}: loads the customer and products referenced by the
 * command, builds the {@link Order} aggregate, places it, and publishes whatever domain events
 * that raised. No business rule lives here, only orchestration; {@code place()}'s invariants
 * and the total computation stay entirely inside {@code Order} itself.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public final class CreateOrderService implements CreateOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;
    private final CustomerRepositoryPort customerRepositoryPort;
    private final DomainEventPublisherPort domainEventPublisherPort;

    public CreateOrderService(OrderRepositoryPort orderRepositoryPort, ProductRepositoryPort productRepositoryPort,
            CustomerRepositoryPort customerRepositoryPort, DomainEventPublisherPort domainEventPublisherPort) {
        this.orderRepositoryPort = Objects.requireNonNull(orderRepositoryPort, "orderRepositoryPort must not be null");
        this.productRepositoryPort = Objects.requireNonNull(productRepositoryPort,
                "productRepositoryPort must not be null");
        this.customerRepositoryPort = Objects.requireNonNull(customerRepositoryPort,
                "customerRepositoryPort must not be null");
        this.domainEventPublisherPort = Objects.requireNonNull(domainEventPublisherPort,
                "domainEventPublisherPort must not be null");
    }

    /**
     * Saves the order twice on purpose: {@code Order.place()} requires an id, which only exists
     * once the database-generated primary key has been assigned by a first {@code save()}. The
     * second {@code save()} persists the resulting {@code PLACED} status and {@code placedAt}.
     */
    @Override
    public Order createOrder(CreateOrderCommand command) {
        Customer customer = customerRepositoryPort.findById(command.customerId())
                .orElseThrow(() -> new CustomerNotFoundException(command.customerId()));

        List<OrderLine> lines = command.lines().stream().map(this::toOrderLine).toList();

        Order order = Order.create(customer.getId(), lines);
        order = orderRepositoryPort.save(order);
        order.place();
        order = orderRepositoryPort.save(order);

        order.pullDomainEvents().forEach(domainEventPublisherPort::publish);

        return order;
    }

    private OrderLine toOrderLine(CreateOrderLineCommand lineCommand) {
        Product product = productRepositoryPort.findById(lineCommand.productId())
                .orElseThrow(() -> new ProductNotFoundException(lineCommand.productId()));
        return OrderLine.of(product.getId(), product.getName(), lineCommand.quantity(), product.getUnitPrice());
    }
}
