package com.edgareldy.domain.exception;

/**
 * Raised when an order references a customer id that does not exist. Same reasoning as
 * {@link CategoryNotFoundException}: a business rule violation on creation, not a plain input
 * error.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public class CustomerNotFoundException extends DomainException {

    public CustomerNotFoundException(Long customerId) {
        super("no customer found with id " + customerId);
    }
}
