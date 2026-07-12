package com.edgareldy.infrastructure.out.persistence.adapter;

import com.edgareldy.domain.model.category.Category;
import com.edgareldy.domain.model.shared.PageResult;
import com.edgareldy.domain.port.in.command.PageQuery;
import com.edgareldy.domain.port.out.CategoryRepositoryPort;
import com.edgareldy.infrastructure.out.persistence.entity.CategoryEntity;
import com.edgareldy.infrastructure.out.persistence.mapper.CategoryPersistenceMapper;
import com.edgareldy.infrastructure.out.persistence.mapper.PageMapper;
import com.edgareldy.infrastructure.out.persistence.repository.CategoryJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;

/**
 * Implements {@link CategoryRepositoryPort} via {@link CategoryJpaRepository}, translating
 * between {@link Category} and {@link CategoryEntity} at the boundary.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
@Repository
public class CategoryRepositoryAdapter implements CategoryRepositoryPort {

    private final CategoryJpaRepository categoryJpaRepository;

    public CategoryRepositoryAdapter(CategoryJpaRepository categoryJpaRepository) {
        this.categoryJpaRepository = Objects.requireNonNull(categoryJpaRepository,
                "categoryJpaRepository must not be null");
    }

    @Override
    public Category save(Category category) {
        CategoryEntity entity = CategoryPersistenceMapper.toEntity(category);
        CategoryEntity saved = categoryJpaRepository.save(entity);
        return CategoryPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryJpaRepository.findById(id).map(CategoryPersistenceMapper::toDomain);
    }

    @Override
    public PageResult<Category> findAll(PageQuery query) {
        return PageMapper.toPageResult(categoryJpaRepository.findAll(PageMapper.toPageable(query)),
                CategoryPersistenceMapper::toDomain);
    }
}
