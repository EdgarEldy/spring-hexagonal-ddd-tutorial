package com.edgareldy.infrastructure.out.persistence.adapter;

import com.edgareldy.domain.model.product.Product;
import com.edgareldy.domain.model.shared.Money;
import com.edgareldy.domain.model.shared.PageResult;
import com.edgareldy.domain.port.in.command.ListProductsQuery;
import com.edgareldy.domain.port.in.command.PageQuery;
import com.edgareldy.infrastructure.PostgresTestcontainersConfiguration;
import com.edgareldy.infrastructure.out.persistence.repository.ProductJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * {@code @DataJpaTest} for {@link ProductRepositoryAdapter}.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
@DataJpaTest
@Import(PostgresTestcontainersConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryAdapterTest {

    private static final Money UNIT_PRICE = new Money(BigDecimal.valueOf(9.99), Currency.getInstance("EUR"));

    @Autowired
    private ProductJpaRepository productJpaRepository;

    private ProductRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new ProductRepositoryAdapter(productJpaRepository);
    }

    @Test
    void saves_and_retrieves_a_product_by_id() {
        Product saved = adapter.save(Product.create(1L, "Mechanical keyboard", UNIT_PRICE));

        Optional<Product> found = adapter.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getUnitPrice()).isEqualTo(UNIT_PRICE);
    }

    @Test
    void filters_by_category_id() {
        adapter.save(Product.create(1L, "Mechanical keyboard", UNIT_PRICE));
        adapter.save(Product.create(2L, "Standing desk", UNIT_PRICE));

        PageResult<Product> page = adapter.findAll(new ListProductsQuery(1L, new PageQuery(0, 10)));

        assertThat(page.content()).hasSize(1);
        assertThat(page.content().get(0).getCategoryId()).isEqualTo(1L);
    }

    @Test
    void lists_every_product_when_no_category_filter_is_given() {
        adapter.save(Product.create(1L, "Mechanical keyboard", UNIT_PRICE));
        adapter.save(Product.create(2L, "Standing desk", UNIT_PRICE));

        PageResult<Product> page = adapter.findAll(new ListProductsQuery(null, new PageQuery(0, 10)));

        assertThat(page.content()).hasSize(2);
    }
}
