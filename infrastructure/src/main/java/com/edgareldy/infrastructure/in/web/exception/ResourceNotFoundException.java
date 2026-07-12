package com.edgareldy.infrastructure.in.web.exception;

/**
 * Raised by a controller when a {@code Get*UseCase} returns an empty {@code Optional}. Not a
 * {@code DomainException}: an absent resource on a direct lookup by id is an HTTP-level concern
 * (mapped to 404 by {@link GlobalExceptionHandler}), not a business rule the domain owns.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
