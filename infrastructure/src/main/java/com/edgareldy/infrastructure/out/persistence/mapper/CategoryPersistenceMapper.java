package com.edgareldy.infrastructure.out.persistence.mapper;

import com.edgareldy.domain.model.category.Category;
import com.edgareldy.infrastructure.out.persistence.entity.CategoryEntity;

/**
 * Manual mapper between {@link Category} and {@link CategoryEntity}. Manual rather than
 * generated: {@code Category} has no public constructor, only the {@code create()}/
 * {@code reconstitute()} factories, so a mapping framework would need custom glue code here
 * anyway.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public final class CategoryPersistenceMapper {

    private CategoryPersistenceMapper() {
    }

    public static Category toDomain(CategoryEntity entity) {
        return Category.reconstitute(entity.getId(), entity.getCategoryName());
    }

    public static CategoryEntity toEntity(Category category) {
        return CategoryEntity.builder()
                .id(category.getId())
                .categoryName(category.getName())
                .build();
    }
}
