package com.edgareldy.domain.port.in.command;

/**
 * Generic pagination input shared by every list use case, so that {@code domain} never depends
 * on Spring Data's {@code Pageable}.
 * <p>
 * Created by edgar.muhamyangabo on 7/11/26
 * Author : edgar.muhamyangabo
 * Date : 7/11/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public record PageQuery(int page, int size) {

    public PageQuery {
        if (page < 0) {
            throw new IllegalArgumentException("page must not be negative");
        }
        if (size < 1) {
            throw new IllegalArgumentException("size must be strictly positive");
        }
    }
}
