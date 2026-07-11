package com.edgareldy.domain.exception;

/**
 * Raised when an order line references a product id that does not exist. Same reasoning as
 * {@link CategoryNotFoundException}: a business rule violation on creation, not a plain input
 * error.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public class ProductNotFoundException extends DomainException {

    public ProductNotFoundException(Long productId) {
        super("no product found with id " + productId);
    }
}
