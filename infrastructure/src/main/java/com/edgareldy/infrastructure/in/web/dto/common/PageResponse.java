package com.edgareldy.infrastructure.in.web.dto.common;

import com.edgareldy.domain.model.shared.PageResult;

import java.util.List;
import java.util.function.Function;

/**
 * Paginated content wrapper used as {@code ApiResponse<PageResponse<T>>} on every list
 * endpoint.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public record PageResponse<T>(List<T> content, int page, int size, long totalElements, int totalPages) {

    public static <D, T> PageResponse<T> from(PageResult<D> pageResult, Function<D, T> toResponse) {
        List<T> content = pageResult.content().stream().map(toResponse).toList();
        return new PageResponse<>(content, pageResult.page(), pageResult.size(), pageResult.totalElements(),
                pageResult.totalPages());
    }
}
