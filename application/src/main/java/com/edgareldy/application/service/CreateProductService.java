package com.edgareldy.application.service;

import com.edgareldy.domain.exception.CategoryNotFoundException;
import com.edgareldy.domain.model.product.Product;
import com.edgareldy.domain.port.in.CreateProductUseCase;
import com.edgareldy.domain.port.in.command.CreateProductCommand;
import com.edgareldy.domain.port.out.CategoryRepositoryPort;
import com.edgareldy.domain.port.out.ProductRepositoryPort;

import java.util.Objects;

/**
 * Implements {@link CreateProductUseCase}: verifies the referenced category exists, then
 * delegates the actual creation to {@link Product#create}. The existence check itself is
 * orchestration (it needs a repository call, which the domain cannot make), not a business
 * rule owned by a specific aggregate.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public final class CreateProductService implements CreateProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;
    private final CategoryRepositoryPort categoryRepositoryPort;

    public CreateProductService(ProductRepositoryPort productRepositoryPort,
            CategoryRepositoryPort categoryRepositoryPort) {
        this.productRepositoryPort = Objects.requireNonNull(productRepositoryPort,
                "productRepositoryPort must not be null");
        this.categoryRepositoryPort = Objects.requireNonNull(categoryRepositoryPort,
                "categoryRepositoryPort must not be null");
    }

    @Override
    public Product createProduct(CreateProductCommand command) {
        categoryRepositoryPort.findById(command.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException(command.categoryId()));
        Product product = Product.create(command.categoryId(), command.name(), command.unitPrice());
        return productRepositoryPort.save(product);
    }
}
