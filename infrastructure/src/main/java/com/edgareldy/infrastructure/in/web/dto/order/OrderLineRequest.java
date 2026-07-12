package com.edgareldy.infrastructure.in.web.dto.order;

/**
 * A single requested line within {@link OrderRequest}. Carries no price, same reasoning as
 * {@code CreateOrderLineCommand}: the unit price is looked up server-side, never trusted from
 * the client.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public record OrderLineRequest(Long productId, int quantity) {
}
