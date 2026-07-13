package com.edgareldy.application.service;

import com.edgareldy.domain.model.category.Category;
import com.edgareldy.domain.model.shared.PageResult;
import com.edgareldy.domain.port.in.ListCategoriesUseCase;
import com.edgareldy.domain.port.in.command.PageQuery;
import com.edgareldy.domain.port.out.CategoryRepositoryPort;

import java.util.Objects;

/**
 * Implements {@link ListCategoriesUseCase}: pure orchestration, no business rule.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public final class ListCategoriesService implements ListCategoriesUseCase {

    private final CategoryRepositoryPort categoryRepositoryPort;

    public ListCategoriesService(CategoryRepositoryPort categoryRepositoryPort) {
        this.categoryRepositoryPort = Objects.requireNonNull(categoryRepositoryPort,
                "categoryRepositoryPort must not be null");
    }

    @Override
    public PageResult<Category> listCategories(PageQuery query) {
        return categoryRepositoryPort.findAll(query);
    }
}
