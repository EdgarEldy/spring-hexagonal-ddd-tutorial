package com.edgareldy.application.service;

import com.edgareldy.domain.exception.CategoryNotFoundException;
import com.edgareldy.domain.model.category.Category;
import com.edgareldy.domain.model.product.Product;
import com.edgareldy.domain.model.shared.Money;
import com.edgareldy.domain.port.in.command.CreateProductCommand;
import com.edgareldy.domain.port.out.CategoryRepositoryPort;
import com.edgareldy.domain.port.out.ProductRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Mockito tests for {@link CreateProductService}: the category existence check and the
 * delegation to {@link ProductRepositoryPort}, with both outbound ports mocked.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
@ExtendWith(MockitoExtension.class)
class CreateProductServiceTest {

    private static final Money UNIT_PRICE = new Money(BigDecimal.valueOf(9.99), Currency.getInstance("EUR"));

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    @Mock
    private CategoryRepositoryPort categoryRepositoryPort;

    @Test
    void creates_and_saves_a_product_when_the_category_exists() {
        when(categoryRepositoryPort.findById(1L)).thenReturn(Optional.of(Category.reconstitute(1L, "Peripherals")));
        Product saved = Product.reconstitute(10L, 1L, "Mechanical keyboard", UNIT_PRICE);
        when(productRepositoryPort.save(any(Product.class))).thenReturn(saved);
        CreateProductService service = new CreateProductService(productRepositoryPort, categoryRepositoryPort);

        Product result = service.createProduct(new CreateProductCommand(1L, "Mechanical keyboard", UNIT_PRICE));

        assertThat(result).isEqualTo(saved);
    }

    @Test
    void rejects_a_product_for_a_nonexistent_category() {
        when(categoryRepositoryPort.findById(1L)).thenReturn(Optional.empty());
        CreateProductService service = new CreateProductService(productRepositoryPort, categoryRepositoryPort);
        CreateProductCommand command = new CreateProductCommand(1L, "Mechanical keyboard", UNIT_PRICE);

        assertThatThrownBy(() -> service.createProduct(command)).isInstanceOf(CategoryNotFoundException.class);

        verify(productRepositoryPort, never()).save(any());
    }
}
