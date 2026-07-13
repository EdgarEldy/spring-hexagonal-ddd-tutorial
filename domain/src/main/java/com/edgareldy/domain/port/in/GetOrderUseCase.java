package com.edgareldy.domain.port.in;

import com.edgareldy.domain.model.order.Order;

import java.util.Optional;

/**
 * Inbound port: retrieve a single order by id.
 * <p>
 * Created by edgar.muhamyangabo on 7/11/26
 * Author : edgar.muhamyangabo
 * Date : 7/11/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public interface GetOrderUseCase {

    Optional<Order> getOrder(Long orderId);
}
