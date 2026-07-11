package com.edgareldy.domain.port.in.command;

import java.util.Objects;

/**
 * Input of {@code CreateCategoryUseCase}.
 * <p>
 * Created by edgar.muhamyangabo on 7/11/26
 * Author : edgar.muhamyangabo
 * Date : 7/11/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public record CreateCategoryCommand(String name) {

    public CreateCategoryCommand {
        Objects.requireNonNull(name, "name must not be null");
    }
}
