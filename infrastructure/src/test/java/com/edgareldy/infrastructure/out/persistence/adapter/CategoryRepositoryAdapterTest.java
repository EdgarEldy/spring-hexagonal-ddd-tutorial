package com.edgareldy.infrastructure.out.persistence.adapter;

import com.edgareldy.domain.model.category.Category;
import com.edgareldy.domain.model.shared.PageResult;
import com.edgareldy.domain.port.in.command.PageQuery;
import com.edgareldy.infrastructure.PostgresTestcontainersConfiguration;
import com.edgareldy.infrastructure.out.persistence.repository.CategoryJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * {@code @DataJpaTest} for {@link CategoryRepositoryAdapter} against a real Postgres container,
 * not an embedded database, so behavior matches production.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
@DataJpaTest
@Import(PostgresTestcontainersConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryRepositoryAdapterTest {

    @Autowired
    private CategoryJpaRepository categoryJpaRepository;

    private CategoryRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new CategoryRepositoryAdapter(categoryJpaRepository);
    }

    @Test
    void saves_and_retrieves_a_category_by_id() {
        Category saved = adapter.save(Category.create("Peripherals"));

        Optional<Category> found = adapter.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(saved.getId());
        assertThat(found.get().getName()).isEqualTo("Peripherals");
    }

    @Test
    void returns_empty_when_not_found() {
        assertThat(adapter.findById(404L)).isEmpty();
    }

    @Test
    void lists_categories_paginated() {
        adapter.save(Category.create("Peripherals"));
        adapter.save(Category.create("Furniture"));

        PageResult<Category> page = adapter.findAll(new PageQuery(0, 10));

        assertThat(page.content()).hasSize(2);
        assertThat(page.totalElements()).isEqualTo(2);
    }
}
