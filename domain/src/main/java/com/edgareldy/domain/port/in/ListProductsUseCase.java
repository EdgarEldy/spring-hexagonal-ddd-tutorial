package com.edgareldy.domain.port.in;

import com.edgareldy.domain.model.product.Product;
import com.edgareldy.domain.model.shared.PageResult;
import com.edgareldy.domain.port.in.command.ListProductsQuery;

/**
 * Inbound port: list products, paginated and optionally filtered by category.
 * <p>
 * Created by edgar.muhamyangabo on 7/11/26
 * Author : edgar.muhamyangabo
 * Date : 7/11/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public interface ListProductsUseCase {

    PageResult<Product> listProducts(ListProductsQuery query);
}
