package com.edgareldy.domain.model.order;

/**
 * Lifecycle states of an {@link Order} aggregate.
 * <p>
 * Created by edgar.muhamyangabo on 7/11/26
 * Author : edgar.muhamyangabo
 * Date : 7/11/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public enum OrderStatus {
    DRAFT,
    PLACED,
    CANCELLED
}
