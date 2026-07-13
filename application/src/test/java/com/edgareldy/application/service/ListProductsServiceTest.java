package com.edgareldy.application.service;

import com.edgareldy.domain.model.product.Product;
import com.edgareldy.domain.model.shared.Money;
import com.edgareldy.domain.model.shared.PageResult;
import com.edgareldy.domain.port.in.command.ListProductsQuery;
import com.edgareldy.domain.port.in.command.PageQuery;
import com.edgareldy.domain.port.out.ProductRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Mockito tests for {@link ListProductsService}: a thin pass-through to
 * {@link ProductRepositoryPort#findAll}.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
@ExtendWith(MockitoExtension.class)
class ListProductsServiceTest {

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    @Test
    void delegates_to_the_repository_port() {
        ListProductsQuery query = new ListProductsQuery(1L, new PageQuery(0, 10));
        Money unitPrice = new Money(BigDecimal.valueOf(9.99), Currency.getInstance("EUR"));
        Product product = Product.reconstitute(10L, 1L, "Mechanical keyboard", unitPrice);
        PageResult<Product> page = new PageResult<>(List.of(product), 0, 10, 1, 1);
        when(productRepositoryPort.findAll(query)).thenReturn(page);
        ListProductsService service = new ListProductsService(productRepositoryPort);

        PageResult<Product> result = service.listProducts(query);

        assertThat(result).isEqualTo(page);
    }
}
