package com.edgareldy.application.service;

import com.edgareldy.domain.model.product.Product;
import com.edgareldy.domain.model.shared.PageResult;
import com.edgareldy.domain.port.in.ListProductsUseCase;
import com.edgareldy.domain.port.in.command.ListProductsQuery;
import com.edgareldy.domain.port.out.ProductRepositoryPort;

import java.util.Objects;

/**
 * Implements {@link ListProductsUseCase}: pure orchestration, no business rule.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public final class ListProductsService implements ListProductsUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    public ListProductsService(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = Objects.requireNonNull(productRepositoryPort,
                "productRepositoryPort must not be null");
    }

    @Override
    public PageResult<Product> listProducts(ListProductsQuery query) {
        return productRepositoryPort.findAll(query);
    }
}
