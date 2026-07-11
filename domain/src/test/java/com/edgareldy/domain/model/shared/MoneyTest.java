package com.edgareldy.domain.model.shared;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for the {@link Money} Value Object: constructor validation and arithmetic.
 * <p>
 * Created by edgar.muhamyangabo on 7/11/26
 * Author : edgar.muhamyangabo
 * Date : 7/11/26
 * Project : spring-hexagonal-ddd-tutorial
 */
class MoneyTest {

    private static final Currency EUR = Currency.getInstance("EUR");
    private static final Currency USD = Currency.getInstance("USD");

    @Test
    void rejects_negative_amount() {
        assertThatThrownBy(() -> new Money(BigDecimal.valueOf(-1), EUR))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void accepts_zero_amount() {
        Money money = new Money(BigDecimal.ZERO, EUR);

        assertThat(money.amount()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void adds_amounts_in_the_same_currency() {
        Money first = new Money(BigDecimal.valueOf(10), EUR);
        Money second = new Money(BigDecimal.valueOf(5), EUR);

        Money total = first.add(second);

        assertThat(total.amount()).isEqualByComparingTo(BigDecimal.valueOf(15));
        assertThat(total.currency()).isEqualTo(EUR);
    }

    @Test
    void rejects_adding_amounts_in_different_currencies() {
        Money first = new Money(BigDecimal.valueOf(10), EUR);
        Money second = new Money(BigDecimal.valueOf(5), USD);

        assertThatThrownBy(() -> first.add(second)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void multiplies_by_a_positive_factor() {
        Money unitPrice = new Money(BigDecimal.valueOf(9.99), EUR);

        Money subtotal = unitPrice.multiply(3);

        assertThat(subtotal.amount()).isEqualByComparingTo(BigDecimal.valueOf(29.97));
    }

    @Test
    void rejects_a_negative_multiplication_factor() {
        Money unitPrice = new Money(BigDecimal.valueOf(9.99), EUR);

        assertThatThrownBy(() -> unitPrice.multiply(-1)).isInstanceOf(IllegalArgumentException.class);
    }
}
