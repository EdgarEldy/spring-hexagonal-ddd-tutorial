package com.edgareldy.domain.model.shared;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for the {@link Email} Value Object: format validation in the constructor.
 * <p>
 * Created by edgar.muhamyangabo on 7/11/26
 * Author : edgar.muhamyangabo
 * Date : 7/11/26
 * Project : spring-hexagonal-ddd-tutorial
 */
class EmailTest {

    @Test
    void accepts_a_well_formed_address() {
        Email email = new Email("customer@example.com");

        assertThat(email.value()).isEqualTo("customer@example.com");
    }

    @ParameterizedTest
    @ValueSource(strings = {"not-an-email", "missing-domain@", "@missing-local.com", "spaces in@address.com"})
    void rejects_a_malformed_address(String invalid) {
        assertThatThrownBy(() -> new Email(invalid)).isInstanceOf(IllegalArgumentException.class);
    }
}
