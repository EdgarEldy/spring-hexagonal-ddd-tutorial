package com.edgareldy.domain.port.in.command;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link CreateOrderCommand}: constructor validation and defensive copy.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
class CreateOrderCommandTest {

    @Test
    void rejects_a_missing_customer_id() {
        assertThatThrownBy(() -> new CreateOrderCommand(null, List.of(new CreateOrderLineCommand(1L, 1))))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void rejects_missing_lines() {
        assertThatThrownBy(() -> new CreateOrderCommand(1L, null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void lines_are_defensively_copied() {
        List<CreateOrderLineCommand> mutable = new ArrayList<>(List.of(new CreateOrderLineCommand(1L, 1)));
        CreateOrderCommand command = new CreateOrderCommand(1L, mutable);

        mutable.add(new CreateOrderLineCommand(2L, 1));

        assertThat(command.lines()).hasSize(1);
    }
}
