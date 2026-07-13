package com.edgareldy.domain.port.in.command;

import com.edgareldy.domain.model.shared.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link CreateProductCommand}: constructor validation.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
class CreateProductCommandTest {

    private static final Money UNIT_PRICE = new Money(BigDecimal.TEN, Currency.getInstance("EUR"));

    @Test
    void rejects_a_missing_category_id() {
        assertThatThrownBy(() -> new CreateProductCommand(null, "Mechanical keyboard", UNIT_PRICE))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void rejects_a_missing_name() {
        assertThatThrownBy(() -> new CreateProductCommand(1L, null, UNIT_PRICE))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void rejects_a_missing_unit_price() {
        assertThatThrownBy(() -> new CreateProductCommand(1L, "Mechanical keyboard", null))
                .isInstanceOf(NullPointerException.class);
    }
}
