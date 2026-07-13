package com.edgareldy.domain.port.out;

import com.edgareldy.domain.model.order.Order;
import com.edgareldy.domain.model.shared.PageResult;
import com.edgareldy.domain.port.in.command.PageQuery;

import java.util.Optional;

/**
 * Outbound port: persistence for the {@code Order} aggregate. Operates only on {@code domain}
 * types, never on DTOs or JPA entities.
 * <p>
 * Created by edgar.muhamyangabo on 7/11/26
 * Author : edgar.muhamyangabo
 * Date : 7/11/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public interface OrderRepositoryPort {

    Order save(Order order);

    Optional<Order> findById(Long id);

    PageResult<Order> findAll(PageQuery query);
}
