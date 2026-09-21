package com.edgareldy.domain.event;

import com.edgareldy.domain.model.shared.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link OrderPlacedEvent}: constructor validation.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
class OrderPlacedEventTest {

    private static final Money TOTAL = new Money(BigDecimal.TEN, Currency.getInstance("EUR"));

    @Test
    void _01_ShouldRejectEvent_WhenOrderIdIsMissing() {
        assertThatThrownBy(() -> new OrderPlacedEvent(null, 1L, TOTAL, Instant.now()))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void _02_ShouldRejectEvent_WhenCustomerIdIsMissing() {
        assertThatThrownBy(() -> new OrderPlacedEvent(1L, null, TOTAL, Instant.now()))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void _03_ShouldRejectEvent_WhenTotalIsMissing() {
        assertThatThrownBy(() -> new OrderPlacedEvent(1L, 1L, null, Instant.now()))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void _04_ShouldRejectEvent_WhenOccurredOnIsMissing() {
        assertThatThrownBy(() -> new OrderPlacedEvent(1L, 1L, TOTAL, null))
                .isInstanceOf(NullPointerException.class);
    }
}
