package com.edgareldy.domain.model.product;

import com.edgareldy.domain.model.shared.Money;

import java.util.Objects;

/**
 * Entity representing a sellable product, belonging to a category. Identity is carried by
 * {@code id}, not by field values.
 * <p>
 * Created by edgar.muhamyangabo on 7/11/26
 * Author : edgar.muhamyangabo
 * Date : 7/11/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public final class Product {

    private final Long id;
    private final Long categoryId;
    private final String name;
    private final Money unitPrice;

    private Product(Long id, Long categoryId, String name, Money unitPrice) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.unitPrice = unitPrice;
    }

    public static Product create(Long categoryId, String name, Money unitPrice) {
        return new Product(null, requireValidCategoryId(categoryId), requireValidName(name),
                requireValidUnitPrice(unitPrice));
    }

    public static Product reconstitute(Long id, Long categoryId, String name, Money unitPrice) {
        Objects.requireNonNull(id, "id must not be null");
        return new Product(id, requireValidCategoryId(categoryId), requireValidName(name),
                requireValidUnitPrice(unitPrice));
    }

    private static Long requireValidCategoryId(Long categoryId) {
        return Objects.requireNonNull(categoryId, "categoryId must not be null");
    }

    private static String requireValidName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        return name;
    }

    private static Money requireValidUnitPrice(Money unitPrice) {
        return Objects.requireNonNull(unitPrice, "unitPrice must not be null");
    }

    public Long getId() {
        return id;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public Money getUnitPrice() {
        return unitPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product other)) {
            return false;
        }
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
