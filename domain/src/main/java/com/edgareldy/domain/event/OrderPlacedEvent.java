package com.edgareldy.domain.event;

import com.edgareldy.domain.model.shared.Money;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain Event raised by the {@code Order} aggregate itself when an order transitions to
 * {@code PLACED}.
 * <p>
 * Created by edgar.muhamyangabo on 7/11/26
 * Author : edgar.muhamyangabo
 * Date : 7/11/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public record OrderPlacedEvent(Long orderId, Long customerId, Money total, Instant occurredOn) implements DomainEvent {

    public OrderPlacedEvent {
        Objects.requireNonNull(orderId, "orderId must not be null");
        Objects.requireNonNull(customerId, "customerId must not be null");
        Objects.requireNonNull(total, "total must not be null");
        Objects.requireNonNull(occurredOn, "occurredOn must not be null");
    }
}
