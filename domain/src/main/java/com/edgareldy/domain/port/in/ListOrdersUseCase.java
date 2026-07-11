package com.edgareldy.domain.port.in;

import com.edgareldy.domain.model.order.Order;
import com.edgareldy.domain.model.shared.PageResult;
import com.edgareldy.domain.port.in.command.PageQuery;

/**
 * Inbound port: list orders, paginated.
 * <p>
 * Created by edgar.muhamyangabo on 7/11/26
 * Author : edgar.muhamyangabo
 * Date : 7/11/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public interface ListOrdersUseCase {

    PageResult<Order> listOrders(PageQuery query);
}
