package com.edgareldy.domain.model.order;

import com.edgareldy.domain.model.shared.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link OrderLine}: subtotal computation and constructor validation.
 * <p>
 * Created by edgar.muhamyangabo on 7/11/26
 * Author : edgar.muhamyangabo
 * Date : 7/11/26
 * Project : spring-hexagonal-ddd-tutorial
 */
class OrderLineTest {

    private static final Currency EUR = Currency.getInstance("EUR");

    @Test
    void computes_subtotal_from_unit_price_and_quantity() {
        Money unitPrice = new Money(BigDecimal.valueOf(9.99), EUR);

        OrderLine line = OrderLine.of(1L, "Mechanical keyboard", 3, unitPrice);

        assertThat(line.getSubtotal().amount()).isEqualByComparingTo(BigDecimal.valueOf(29.97));
    }

    @Test
    void rejects_a_zero_or_negative_quantity() {
        Money unitPrice = new Money(BigDecimal.valueOf(9.99), EUR);

        assertThatThrownBy(() -> OrderLine.of(1L, "Mechanical keyboard", 0, unitPrice))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejects_a_blank_product_name() {
        Money unitPrice = new Money(BigDecimal.valueOf(9.99), EUR);

        assertThatThrownBy(() -> OrderLine.of(1L, " ", 1, unitPrice))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
