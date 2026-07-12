package com.edgareldy.infrastructure.in.web.dto.order;

import com.edgareldy.infrastructure.in.web.dto.common.MoneyDto;

/**
 * A single line within {@link OrderResponse}.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public record OrderLineResponse(Long productId, String productName, int quantity, MoneyDto unitPrice,
        MoneyDto subtotal) {
}
