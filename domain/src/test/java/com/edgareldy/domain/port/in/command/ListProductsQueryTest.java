package com.edgareldy.domain.port.in.command;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link ListProductsQuery}: constructor validation.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
class ListProductsQueryTest {

    @Test
    void rejects_a_missing_page() {
        assertThatThrownBy(() -> new ListProductsQuery(1L, null)).isInstanceOf(NullPointerException.class);
    }
}
