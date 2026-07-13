package com.edgareldy.domain.port.in.command;

import java.util.Objects;

/**
 * Input of {@code ListProductsUseCase}: pagination plus an optional category filter.
 * <p>
 * Created by edgar.muhamyangabo on 7/11/26
 * Author : edgar.muhamyangabo
 * Date : 7/11/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public record ListProductsQuery(Long categoryId, PageQuery page) {

    public ListProductsQuery {
        Objects.requireNonNull(page, "page must not be null");
    }
}
