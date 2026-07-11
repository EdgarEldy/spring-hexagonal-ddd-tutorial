package com.edgareldy.domain.port.in.command;

import java.util.List;
import java.util.Objects;

/**
 * Input of {@code CreateOrderUseCase}.
 * <p>
 * Created by edgar.muhamyangabo on 7/11/26
 * Author : edgar.muhamyangabo
 * Date : 7/11/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public record CreateOrderCommand(Long customerId, List<CreateOrderLineCommand> lines) {

    public CreateOrderCommand {
        Objects.requireNonNull(customerId, "customerId must not be null");
        Objects.requireNonNull(lines, "lines must not be null");
        lines = List.copyOf(lines);
    }
}
