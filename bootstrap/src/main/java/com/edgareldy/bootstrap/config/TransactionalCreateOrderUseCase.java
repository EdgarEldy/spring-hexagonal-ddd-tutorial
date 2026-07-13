package com.edgareldy.bootstrap.config;

import com.edgareldy.domain.model.order.Order;
import com.edgareldy.domain.port.in.CreateOrderUseCase;
import com.edgareldy.domain.port.in.command.CreateOrderCommand;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Wraps the real {@code CreateOrderService} in a transactional boundary. {@code application}
 * carries no Spring dependency, so {@code @Transactional} cannot be placed directly on the
 * service; this decorator is the bean actually exposed to {@code OrderController}, and Spring's
 * transaction-management proxy applies to it like any other bean regardless of having been
 * constructed with {@code new} inside a {@code @Bean} factory method rather than
 * component-scanned. Needed because {@code CreateOrderService.createOrder()} calls
 * {@code OrderRepositoryPort.save()} twice (once to obtain the database-generated id before
 * {@code place()} can run, once more to persist the resulting {@code PLACED} status): without
 * this boundary, a failure on the second {@code save()} would leave a {@code DRAFT} order
 * permanently persisted, since each call would otherwise run in its own auto-committed
 * transaction.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public class TransactionalCreateOrderUseCase implements CreateOrderUseCase {

    private final CreateOrderUseCase delegate;

    public TransactionalCreateOrderUseCase(CreateOrderUseCase delegate) {
        this.delegate = Objects.requireNonNull(delegate, "delegate must not be null");
    }

    @Override
    @Transactional
    public Order createOrder(CreateOrderCommand command) {
        return delegate.createOrder(command);
    }
}
