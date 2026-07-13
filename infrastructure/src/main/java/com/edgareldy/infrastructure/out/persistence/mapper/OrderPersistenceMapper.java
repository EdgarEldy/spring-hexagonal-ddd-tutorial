package com.edgareldy.infrastructure.out.persistence.mapper;

import com.edgareldy.domain.model.order.Order;
import com.edgareldy.domain.model.order.OrderLine;
import com.edgareldy.domain.model.order.OrderStatus;
import com.edgareldy.domain.model.shared.Money;
import com.edgareldy.infrastructure.out.persistence.entity.OrderEntity;
import com.edgareldy.infrastructure.out.persistence.entity.OrderLineEntity;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

/**
 * Manual mapper between {@link Order} and {@link OrderEntity}. {@code toDomain} always goes
 * through {@code Order.reconstitute}, never {@code Order.create}: a row read back from the
 * database is, by definition, already persisted, and must not re-raise {@code OrderPlacedEvent}
 * the way a fresh aggregate would.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public final class OrderPersistenceMapper {

    private OrderPersistenceMapper() {
    }

    public static Order toDomain(OrderEntity entity) {
        List<OrderLine> lines = entity.getLines().stream().map(OrderPersistenceMapper::lineToDomain).toList();
        return Order.reconstitute(entity.getId(), entity.getCustomerId(), lines,
                OrderStatus.valueOf(entity.getStatus()), entity.getPlacedAt());
    }

    private static OrderLine lineToDomain(OrderLineEntity lineEntity) {
        Money unitPrice = new Money(lineEntity.getUnitPriceAmount(),
                Currency.getInstance(lineEntity.getUnitPriceCurrency()));
        return OrderLine.of(lineEntity.getProductId(), lineEntity.getProductName(), lineEntity.getQuantity(),
                unitPrice);
    }

    /**
     * Builds a brand new entity, lines included, meant for the initial insert only. Updating an
     * already-persisted order goes through {@link #applyStatusChange} instead, which never
     * touches the lines collection.
     */
    public static OrderEntity toNewEntity(Order order) {
        OrderEntity entity = toEntityWithoutLines(order);
        List<OrderLineEntity> lineEntities = new ArrayList<>();
        for (OrderLine line : order.getLines()) {
            lineEntities.add(lineToEntity(line, entity));
        }
        entity.setLines(lineEntities);
        return entity;
    }

    /**
     * Copies the order's current status/total/placedAt onto an already-loaded, managed entity,
     * deliberately leaving {@code lines} untouched: no use case in this project mutates an
     * order's lines after creation, so there is nothing to reconcile, and touching the
     * collection here would make Hibernate delete and re-insert every line for no reason.
     */
    public static void applyStatusChange(OrderEntity entity, Order order) {
        entity.setStatus(order.getStatus().name());
        entity.setPlacedAt(order.getPlacedAt());
        Money total = order.getTotal();
        entity.setTotalAmount(total.amount());
        entity.setTotalCurrency(total.currency().getCurrencyCode());
    }

    private static OrderEntity toEntityWithoutLines(Order order) {
        Money total = order.getTotal();
        return OrderEntity.builder()
                .id(order.getId())
                .customerId(order.getCustomerId())
                .status(order.getStatus().name())
                .totalAmount(total.amount())
                .totalCurrency(total.currency().getCurrencyCode())
                .placedAt(order.getPlacedAt())
                .build();
    }

    private static OrderLineEntity lineToEntity(OrderLine line, OrderEntity parent) {
        return OrderLineEntity.builder()
                .order(parent)
                .productId(line.getProductId())
                .productName(line.getProductName())
                .quantity(line.getQuantity())
                .unitPriceAmount(line.getUnitPrice().amount())
                .unitPriceCurrency(line.getUnitPrice().currency().getCurrencyCode())
                .build();
    }
}
