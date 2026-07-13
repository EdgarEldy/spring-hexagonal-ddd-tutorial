package com.edgareldy.domain.port.in.command;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link CreateCategoryCommand}: constructor validation.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
class CreateCategoryCommandTest {

    @Test
    void rejects_a_missing_name() {
        assertThatThrownBy(() -> new CreateCategoryCommand(null)).isInstanceOf(NullPointerException.class);
    }
}
