package com.edgareldy.application.service;

import com.edgareldy.domain.model.category.Category;
import com.edgareldy.domain.port.in.CreateCategoryUseCase;
import com.edgareldy.domain.port.in.command.CreateCategoryCommand;
import com.edgareldy.domain.port.out.CategoryRepositoryPort;

import java.util.Objects;

/**
 * Implements {@link CreateCategoryUseCase}: pure orchestration, no business rule. Plain Java,
 * no Spring annotation, per the project's choice to keep {@code application} entirely
 * framework-free; {@code bootstrap} wires this class as a bean explicitly.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public final class CreateCategoryService implements CreateCategoryUseCase {

    private final CategoryRepositoryPort categoryRepositoryPort;

    public CreateCategoryService(CategoryRepositoryPort categoryRepositoryPort) {
        this.categoryRepositoryPort = Objects.requireNonNull(categoryRepositoryPort,
                "categoryRepositoryPort must not be null");
    }

    @Override
    public Category createCategory(CreateCategoryCommand command) {
        Category category = Category.create(command.name());
        return categoryRepositoryPort.save(category);
    }
}
