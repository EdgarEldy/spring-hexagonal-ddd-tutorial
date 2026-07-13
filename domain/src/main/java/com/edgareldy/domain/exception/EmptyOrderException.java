package com.edgareldy.domain.exception;

/**
 * Raised when an {@code Order} is placed without at least one {@code OrderLine}.
 * <p>
 * Created by edgar.muhamyangabo on 7/11/26
 * Author : edgar.muhamyangabo
 * Date : 7/11/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public class EmptyOrderException extends DomainException {

    public EmptyOrderException() {
        super("an order cannot be placed without at least one line");
    }
}
