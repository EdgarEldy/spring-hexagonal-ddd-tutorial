package com.edgareldy.domain.model.shared;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

/**
 * Immutable Value Object representing a monetary amount in a specific currency.
 * <p>
 * Created by edgar.muhamyangabo on 7/11/26
 * Author : edgar.muhamyangabo
 * Date : 7/11/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public record Money(BigDecimal amount, Currency currency) {

    public Money {
        Objects.requireNonNull(amount, "amount must not be null");
        Objects.requireNonNull(currency, "currency must not be null");
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("amount must not be negative");
        }
    }

    public static Money zero(Currency currency) {
        return new Money(BigDecimal.ZERO, currency);
    }

    public Money add(Money other) {
        requireSameCurrency(other);
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money multiply(long factor) {
        if (factor < 0) {
            throw new IllegalArgumentException("factor must not be negative");
        }
        return new Money(this.amount.multiply(BigDecimal.valueOf(factor)), this.currency);
    }

    private void requireSameCurrency(Money other) {
        Objects.requireNonNull(other, "other must not be null");
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException(
                    "cannot combine amounts in different currencies: " + this.currency + " and " + other.currency);
        }
    }

    /**
     * Overridden rather than left to the record default: {@link BigDecimal#equals} is
     * scale-sensitive ({@code 9.9} and {@code 9.90} compare unequal), which would let two
     * economically identical amounts (e.g. after a {@code multiply()} changes the scale) be
     * treated as different. Equality here follows {@link BigDecimal#compareTo} instead.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Money other)) {
            return false;
        }
        return amount.compareTo(other.amount) == 0 && currency.equals(other.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount.stripTrailingZeros(), currency);
    }
}
