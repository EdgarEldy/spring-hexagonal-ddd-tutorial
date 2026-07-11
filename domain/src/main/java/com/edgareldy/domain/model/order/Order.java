package com.edgareldy.domain.model.order;

import com.edgareldy.domain.event.DomainEvent;
import com.edgareldy.domain.event.OrderPlacedEvent;
import com.edgareldy.domain.exception.EmptyOrderException;
import com.edgareldy.domain.model.shared.Money;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Aggregate Root holding a collection of {@link OrderLine}. Nothing outside this class is
 * allowed to modify a line directly, and {@code total} is never assigned from the outside: it
 * is always recomputed by the aggregate itself from its lines.
 * <p>
 * Created by edgar.muhamyangabo on 7/11/26
 * Author : edgar.muhamyangabo
 * Date : 7/11/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public final class Order {

    private final Long id;
    private final Long customerId;
    private final List<OrderLine> lines;
    private OrderStatus status;
    private Instant placedAt;
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    private Order(Long id, Long customerId, List<OrderLine> lines, OrderStatus status, Instant placedAt) {
        this.id = id;
        this.customerId = customerId;
        this.lines = new ArrayList<>(lines);
        this.status = status;
        this.placedAt = placedAt;
    }

    public static Order create(Long customerId, List<OrderLine> lines) {
        Objects.requireNonNull(customerId, "customerId must not be null");
        Objects.requireNonNull(lines, "lines must not be null");
        if (lines.isEmpty()) {
            throw new EmptyOrderException();
        }
        return new Order(null, customerId, lines, OrderStatus.DRAFT, null);
    }

    public static Order reconstitute(Long id, Long customerId, List<OrderLine> lines, OrderStatus status,
            Instant placedAt) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(status, "status must not be null");
        return new Order(id, customerId, lines, status, placedAt);
    }

    /**
     * Transitions the order to {@code PLACED}, raising {@link OrderPlacedEvent}. The
     * "not empty" invariant is re-checked here, not just relied upon from construction, because
     * this is the method the domain's own ubiquitous language holds responsible for it: any
     * future capability that mutates {@code lines} after creation must still go through this
     * guard before an order can be placed.
     * <p>
     * Requires {@code id} to already be assigned: with a database-generated primary key, a
     * freshly created order must be saved once to obtain its id before it can be placed, so
     * that {@link OrderPlacedEvent} always carries a valid {@code orderId}.
     */
    public void place() {
        if (id == null) {
            throw new IllegalStateException("an order must be saved (assigned an id) before it can be placed");
        }
        if (status != OrderStatus.DRAFT) {
            throw new IllegalStateException("only a DRAFT order can be placed, current status is " + status);
        }
        if (lines.isEmpty()) {
            throw new EmptyOrderException();
        }
        this.status = OrderStatus.PLACED;
        this.placedAt = Instant.now();
        domainEvents.add(new OrderPlacedEvent(id, customerId, getTotal(), placedAt));
    }

    public List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> events = List.copyOf(domainEvents);
        domainEvents.clear();
        return events;
    }

    /**
     * Recomputes the total from the current lines. Returns {@code null} only for a
     * {@code reconstitute()}-loaded order with no lines at all, an anomaly {@code create()}
     * itself never allows: every order built through the normal creation path is guaranteed
     * to have at least one line, hence a non-null total.
     */
    public Money getTotal() {
        Money total = null;
        for (OrderLine line : lines) {
            total = total == null ? line.getSubtotal() : total.add(line.getSubtotal());
        }
        return total;
    }

    public Long getId() {
        return id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public List<OrderLine> getLines() {
        return List.copyOf(lines);
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Instant getPlacedAt() {
        return placedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order other)) {
            return false;
        }
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
