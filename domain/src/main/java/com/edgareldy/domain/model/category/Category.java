package com.edgareldy.domain.model.category;

import java.util.Objects;

/**
 * Entity representing a product category. Identity is carried by {@code id}, not by field
 * values: two instances with the same id are considered equal regardless of their name.
 * <p>
 * Created by edgar.muhamyangabo on 7/11/26
 * Author : edgar.muhamyangabo
 * Date : 7/11/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public final class Category {

    private final Long id;
    private final String name;

    private Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Category create(String name) {
        return new Category(null, requireValidName(name));
    }

    public static Category reconstitute(Long id, String name) {
        Objects.requireNonNull(id, "id must not be null");
        return new Category(id, requireValidName(name));
    }

    private static String requireValidName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        return name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Category other)) {
            return false;
        }
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
