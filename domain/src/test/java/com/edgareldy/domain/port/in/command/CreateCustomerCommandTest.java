package com.edgareldy.domain.port.in.command;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link CreateCustomerCommand}: constructor validation.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
class CreateCustomerCommandTest {

    @Test
    void rejects_a_missing_email() {
        assertThatThrownBy(() -> new CreateCustomerCommand("Jane", "Doe", "0102030405", null, "1 rue de Paris"))
                .isInstanceOf(NullPointerException.class);
    }
}
