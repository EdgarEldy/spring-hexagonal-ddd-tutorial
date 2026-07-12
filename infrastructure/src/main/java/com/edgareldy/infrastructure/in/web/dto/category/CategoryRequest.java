package com.edgareldy.infrastructure.in.web.dto.category;

/**
 * Request body of {@code POST /api/v1/categories}.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public record CategoryRequest(String name) {
}
