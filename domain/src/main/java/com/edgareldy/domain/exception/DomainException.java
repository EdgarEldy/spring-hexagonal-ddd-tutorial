package com.edgareldy.domain.exception;

/**
 * Base type for every business rule violation raised by the domain. Infrastructure translates
 * each subclass into the appropriate HTTP status; the domain itself knows nothing about HTTP.
 * <p>
 * Created by edgar.muhamyangabo on 7/11/26
 * Author : edgar.muhamyangabo
 * Date : 7/11/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public abstract class DomainException extends RuntimeException {

    protected DomainException(String message) {
        super(message);
    }
}
