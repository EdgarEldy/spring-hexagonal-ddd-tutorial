package com.edgareldy.application.service;

import com.edgareldy.domain.model.category.Category;
import com.edgareldy.domain.port.in.command.CreateCategoryCommand;
import com.edgareldy.domain.port.out.CategoryRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Mockito tests for {@link CreateCategoryService}: verifies orchestration only, the
 * {@link CategoryRepositoryPort} is mocked, no Spring context, no database.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
@ExtendWith(MockitoExtension.class)
class CreateCategoryServiceTest {

    @Mock
    private CategoryRepositoryPort categoryRepositoryPort;

    @Test
    void creates_and_saves_a_category() {
        Category saved = Category.reconstitute(1L, "Peripherals");
        when(categoryRepositoryPort.save(any(Category.class))).thenReturn(saved);
        CreateCategoryService service = new CreateCategoryService(categoryRepositoryPort);

        Category result = service.createCategory(new CreateCategoryCommand("Peripherals"));

        assertThat(result).isEqualTo(saved);
        verify(categoryRepositoryPort).save(any(Category.class));
    }
}
