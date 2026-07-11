package com.edgareldy.domain.model.order;

import com.edgareldy.domain.model.shared.Money;

import java.util.Objects;

/**
 * A single line of an {@link Order}, owned by the aggregate: nothing outside {@code Order} is
 * allowed to create or mutate an {@code OrderLine} directly. The unit price is captured at line
 * creation time, not re-read from the product later, so a price change never alters an existing
 * order's total.
 * <p>
 * Created by edgar.muhamyangabo on 7/11/26
 * Author : edgar.muhamyangabo
 * Date : 7/11/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public final class OrderLine {

    private final Long productId;
    private final String productName;
    private final int quantity;
    private final Money unitPrice;

    private OrderLine(Long productId, String productName, int quantity, Money unitPrice) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public static OrderLine of(Long productId, String productName, int quantity, Money unitPrice) {
        Objects.requireNonNull(productId, "productId must not be null");
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("productName must not be blank");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be strictly positive");
        }
        Objects.requireNonNull(unitPrice, "unitPrice must not be null");
        return new OrderLine(productId, productName, quantity, unitPrice);
    }

    public Money getSubtotal() {
        return unitPrice.multiply(quantity);
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public Money getUnitPrice() {
        return unitPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderLine other)) {
            return false;
        }
        return quantity == other.quantity && productId.equals(other.productId) && productName.equals(
                other.productName) && unitPrice.equals(other.unitPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, productName, quantity, unitPrice);
    }
}
