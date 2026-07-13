package com.edgareldy.domain.port.in.command;

import java.util.Objects;

/**
 * A single requested line of {@code CreateOrderCommand}. Carries no price: the unit price is
 * looked up server-side from the current {@code Product}, never supplied by the client.
 * <p>
 * Created by edgar.muhamyangabo on 7/11/26
 * Author : edgar.muhamyangabo
 * Date : 7/11/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public record CreateOrderLineCommand(Long productId, int quantity) {

    public CreateOrderLineCommand {
        Objects.requireNonNull(productId, "productId must not be null");
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be strictly positive");
        }
    }
}
