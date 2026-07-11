package com.edgareldy.domain.port.in;

import com.edgareldy.domain.model.order.Order;
import com.edgareldy.domain.port.in.command.CreateOrderCommand;

/**
 * Inbound port: create and place an order made of one or more lines.
 * <p>
 * Created by edgar.muhamyangabo on 7/11/26
 * Author : edgar.muhamyangabo
 * Date : 7/11/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public interface CreateOrderUseCase {

    Order createOrder(CreateOrderCommand command);
}
