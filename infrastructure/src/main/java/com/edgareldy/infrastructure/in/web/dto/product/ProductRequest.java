package com.edgareldy.infrastructure.in.web.dto.product;

import com.edgareldy.infrastructure.in.web.dto.common.MoneyDto;

/**
 * Request body of {@code POST /api/v1/products}.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public record ProductRequest(Long categoryId, String name, MoneyDto unitPrice) {
}
