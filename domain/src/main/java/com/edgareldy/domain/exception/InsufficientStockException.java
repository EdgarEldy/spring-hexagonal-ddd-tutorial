package com.edgareldy.domain.exception;

/**
 * Raised when an order line requests more units of a product than are available. Reserved for
 * a future stock-tracking invariant: the current {@code Product} model carries no stock
 * quantity yet, so nothing raises this exception in {@code feature/domain}.
 * <p>
 * Created by edgar.muhamyangabo on 7/11/26
 * Author : edgar.muhamyangabo
 * Date : 7/11/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public class InsufficientStockException extends DomainException {

    public InsufficientStockException(String message) {
        super(message);
    }
}
