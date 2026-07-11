package com.edgareldy.domain.model.customer;

import com.edgareldy.domain.model.shared.Email;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link Customer}: constructor validation and identity-based equality.
 * <p>
 * Created by edgar.muhamyangabo on 7/11/26
 * Author : edgar.muhamyangabo
 * Date : 7/11/26
 * Project : spring-hexagonal-ddd-tutorial
 */
class CustomerTest {

    private static final Email EMAIL = new Email("customer@example.com");

    @Test
    void rejects_a_blank_first_name() {
        assertThatThrownBy(() -> Customer.create(" ", "Doe", "0102030405", EMAIL, "1 rue de Paris"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void two_instances_with_the_same_id_are_equal_regardless_of_fields() {
        Customer first = Customer.reconstitute(1L, "Jane", "Doe", "0102030405", EMAIL, "1 rue de Paris");
        Customer second = Customer.reconstitute(1L, "John", "Smith", "0605040302", EMAIL, "2 rue de Lyon");

        assertThat(first).isEqualTo(second);
    }
}
