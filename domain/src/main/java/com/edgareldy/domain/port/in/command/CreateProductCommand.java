package com.edgareldy.domain.port.in.command;

import com.edgareldy.domain.model.shared.Money;

import java.util.Objects;

/**
 * Input of {@code CreateProductUseCase}.
 * <p>
 * Created by edgar.muhamyangabo on 7/11/26
 * Author : edgar.muhamyangabo
 * Date : 7/11/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public record CreateProductCommand(Long categoryId, String name, Money unitPrice) {

    public CreateProductCommand {
        Objects.requireNonNull(categoryId, "categoryId must not be null");
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(unitPrice, "unitPrice must not be null");
    }
}
