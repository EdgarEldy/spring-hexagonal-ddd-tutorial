package com.edgareldy.domain.port.out;

import com.edgareldy.domain.model.category.Category;
import com.edgareldy.domain.model.shared.PageResult;
import com.edgareldy.domain.port.in.command.PageQuery;

import java.util.Optional;

/**
 * Outbound port: persistence for {@code Category}. Operates only on {@code domain} types, never
 * on DTOs or JPA entities. {@code findById} exists for {@code CreateProductUseCase} to verify
 * the referenced category exists before creating a product.
 * <p>
 * Created by edgar.muhamyangabo on 7/11/26
 * Author : edgar.muhamyangabo
 * Date : 7/11/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public interface CategoryRepositoryPort {

    Category save(Category category);

    Optional<Category> findById(Long id);

    PageResult<Category> findAll(PageQuery query);
}
