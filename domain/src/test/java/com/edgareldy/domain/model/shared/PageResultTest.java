package com.edgareldy.domain.model.shared;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link PageResult}: constructor validation and defensive copy of content.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
class PageResultTest {

    @Test
    void rejects_null_content() {
        assertThatThrownBy(() -> new PageResult<String>(null, 0, 10, 0, 0))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void content_is_defensively_copied() {
        List<String> mutable = new ArrayList<>(List.of("a", "b"));
        PageResult<String> page = new PageResult<>(mutable, 0, 10, 2, 1);

        mutable.add("c");

        assertThat(page.content()).containsExactly("a", "b");
    }
}
