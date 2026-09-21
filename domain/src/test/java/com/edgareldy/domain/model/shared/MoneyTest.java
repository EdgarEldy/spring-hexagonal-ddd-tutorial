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
    void _01_ShouldRejectMoney_WhenAmountIsNegative() {
        assertThatThrownBy(() -> new Money(BigDecimal.valueOf(-1), EUR))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void _02_ShouldAcceptMoney_WhenAmountIsZero() {
        Money money = new Money(BigDecimal.ZERO, EUR);

        assertThat(money.amount()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void _03_ShouldAddAmounts_WhenCurrenciesMatch() {
        Money first = new Money(BigDecimal.valueOf(10), EUR);
        Money second = new Money(BigDecimal.valueOf(5), EUR);

        Money total = first.add(second);

        assertThat(total.amount()).isEqualByComparingTo(BigDecimal.valueOf(15));
        assertThat(total.currency()).isEqualTo(EUR);
    }

    @Test
    void _04_ShouldRejectAddition_WhenCurrenciesDiffer() {
        Money first = new Money(BigDecimal.valueOf(10), EUR);
        Money second = new Money(BigDecimal.valueOf(5), USD);

        assertThatThrownBy(() -> first.add(second)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void _05_ShouldMultiplyAmount_WhenFactorIsPositive() {
        Money unitPrice = new Money(BigDecimal.valueOf(9.99), EUR);

        Money subtotal = unitPrice.multiply(3);

        assertThat(subtotal.amount()).isEqualByComparingTo(BigDecimal.valueOf(29.97));
    }

    @Test
    void _06_ShouldRejectMultiplication_WhenFactorIsNegative() {
        Money unitPrice = new Money(BigDecimal.valueOf(9.99), EUR);

        assertThatThrownBy(() -> unitPrice.multiply(-1)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void _07_ShouldReturnZeroAmount_WhenZeroIsRequestedInACurrency() {
        Money zero = Money.zero(EUR);

        assertThat(zero.amount()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(zero.currency()).isEqualTo(EUR);
    }

    @Test
    void _08_ShouldBeEqual_WhenAmountsHaveDifferentScaleButSameValue() {
        Money first = new Money(new BigDecimal("9.90"), EUR);
        Money second = new Money(new BigDecimal("9.9"), EUR);

        assertThat(first).isEqualTo(second);
        assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }
}
