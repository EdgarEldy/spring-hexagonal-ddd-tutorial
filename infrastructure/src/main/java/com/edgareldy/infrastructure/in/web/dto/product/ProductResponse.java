package com.edgareldy.infrastructure.in.web.dto.product;

import com.edgareldy.infrastructure.in.web.dto.common.MoneyDto;

/**
 * Response body for a product.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public record ProductResponse(Long id, Long categoryId, String name, MoneyDto unitPrice) {
}
