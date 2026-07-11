package com.edgareldy.application.service;

import com.edgareldy.domain.model.category.Category;
import com.edgareldy.domain.model.shared.PageResult;
import com.edgareldy.domain.port.in.command.PageQuery;
import com.edgareldy.domain.port.out.CategoryRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Mockito tests for {@link ListCategoriesService}: a thin pass-through to
 * {@link CategoryRepositoryPort#findAll}.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
@ExtendWith(MockitoExtension.class)
class ListCategoriesServiceTest {

    @Mock
    private CategoryRepositoryPort categoryRepositoryPort;

    @Test
    void delegates_to_the_repository_port() {
        PageQuery query = new PageQuery(0, 10);
        PageResult<Category> page = new PageResult<>(List.of(Category.reconstitute(1L, "Peripherals")), 0, 10, 1, 1);
        when(categoryRepositoryPort.findAll(query)).thenReturn(page);
        ListCategoriesService service = new ListCategoriesService(categoryRepositoryPort);

        PageResult<Category> result = service.listCategories(query);

        assertThat(result).isEqualTo(page);
    }
}
