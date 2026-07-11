package com.edgareldy.domain.port.out;

import com.edgareldy.domain.model.product.Product;
import com.edgareldy.domain.model.shared.PageResult;
import com.edgareldy.domain.port.in.command.ListProductsQuery;

import java.util.Optional;

/**
 * Outbound port: persistence for {@code Product}. Operates only on {@code domain} types, never
 * on DTOs or JPA entities.
 * <p>
 * Created by edgar.muhamyangabo on 7/11/26
 * Author : edgar.muhamyangabo
 * Date : 7/11/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public interface ProductRepositoryPort {

    Product save(Product product);

    Optional<Product> findById(Long id);

    PageResult<Product> findAll(ListProductsQuery query);
}
