package com.edgareldy.infrastructure.in.web.dto.order;

import com.edgareldy.infrastructure.in.web.dto.common.MoneyDto;

import java.time.Instant;
import java.util.List;

/**
 * Response body for an order.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public record OrderResponse(Long id, Long customerId, String status, List<OrderLineResponse> lines, MoneyDto total,
        Instant placedAt) {
}
