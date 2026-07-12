package com.edgareldy.infrastructure.in.web.dto.order;

import java.util.List;

/**
 * Request body of {@code POST /api/v1/orders}.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public record OrderRequest(Long customerId, List<OrderLineRequest> lines) {
}
