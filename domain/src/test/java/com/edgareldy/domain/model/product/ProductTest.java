package com.edgareldy.domain.model.product;

import com.edgareldy.domain.model.shared.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link Product}: constructor validation and identity-based equality.
 * <p>
 * Created by edgar.muhamyangabo on 7/11/26
 * Author : edgar.muhamyangabo
 * Date : 7/11/26
 * Project : spring-hexagonal-ddd-tutorial
 */
class ProductTest {

    private static final Money UNIT_PRICE = new Money(BigDecimal.valueOf(9.99), Currency.getInstance("EUR"));

    @Test
    void _01_ShouldRejectProduct_WhenCategoryIsMissing() {
        assertThatThrownBy(() -> Product.create(null, "Mechanical keyboard", UNIT_PRICE))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void _02_ShouldRejectProduct_WhenNameIsBlank() {
        assertThatThrownBy(() -> Product.create(1L, " ", UNIT_PRICE)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void _03_ShouldBeEqual_WhenInstancesShareTheSameIdRegardlessOfFields() {
        Product first = Product.reconstitute(1L, 1L, "Mechanical keyboard", UNIT_PRICE);
        Product second = Product.reconstitute(1L, 2L, "Renamed", UNIT_PRICE);

        assertThat(first).isEqualTo(second);
    }
}
