package com.edgareldy.domain.model.shared;

import java.util.List;
import java.util.Objects;

/**
 * Domain-native, framework-agnostic pagination result, returned by inbound and outbound ports
 * so that {@code domain} never depends on Spring Data's {@code Page}.
 * <p>
 * Created by edgar.muhamyangabo on 7/11/26
 * Author : edgar.muhamyangabo
 * Date : 7/11/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public record PageResult<T>(List<T> content, int page, int size, long totalElements, int totalPages) {

    public PageResult {
        Objects.requireNonNull(content, "content must not be null");
        content = List.copyOf(content);
    }
}
