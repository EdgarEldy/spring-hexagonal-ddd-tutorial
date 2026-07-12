package com.edgareldy.infrastructure.in.web.mapper;

import com.edgareldy.domain.model.category.Category;
import com.edgareldy.domain.port.in.command.CreateCategoryCommand;
import com.edgareldy.infrastructure.in.web.dto.category.CategoryRequest;
import com.edgareldy.infrastructure.in.web.dto.category.CategoryResponse;

/**
 * Manual mapper between the category web DTOs and the domain, kept out of {@code domain}/
 * {@code application}: entities and domain objects never cross into HTTP responses directly.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public final class CategoryWebMapper {

    private CategoryWebMapper() {
    }

    public static CreateCategoryCommand toCommand(CategoryRequest request) {
        return new CreateCategoryCommand(request.name());
    }

    public static CategoryResponse toResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getName());
    }
}
