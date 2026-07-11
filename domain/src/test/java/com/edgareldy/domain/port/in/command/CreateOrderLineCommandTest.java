package com.edgareldy.domain.port.in.command;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link CreateOrderLineCommand}: constructor validation.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
class CreateOrderLineCommandTest {

    @Test
    void rejects_a_missing_product_id() {
        assertThatThrownBy(() -> new CreateOrderLineCommand(null, 1)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void rejects_a_zero_or_negative_quantity() {
        assertThatThrownBy(() -> new CreateOrderLineCommand(1L, 0)).isInstanceOf(IllegalArgumentException.class);
    }
}
