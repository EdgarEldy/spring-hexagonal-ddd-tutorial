package com.edgareldy.domain.model.category;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link Category}: constructor validation and identity-based equality.
 * <p>
 * Created by edgar.muhamyangabo on 7/11/26
 * Author : edgar.muhamyangabo
 * Date : 7/11/26
 * Project : spring-hexagonal-ddd-tutorial
 */
class CategoryTest {

    @Test
    void rejects_a_blank_name() {
        assertThatThrownBy(() -> Category.create(" ")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void two_instances_with_the_same_id_are_equal_regardless_of_name() {
        Category first = Category.reconstitute(1L, "Peripherals");
        Category second = Category.reconstitute(1L, "Renamed");

        assertThat(first).isEqualTo(second);
    }

    @Test
    void a_newly_created_category_has_no_id_yet() {
        Category category = Category.create("Peripherals");

        assertThat(category.getId()).isNull();
    }
}
