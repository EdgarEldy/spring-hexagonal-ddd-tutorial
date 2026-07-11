package com.edgareldy.domain.exception;

/**
 * Raised when a command references a category id that does not exist, discovered while
 * implementing {@code CreateProductUseCase}: creating a product for a nonexistent category is
 * a business rule violation, not a mere input format error, so it is a {@link DomainException}
 * rather than a plain {@code IllegalArgumentException}.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public class CategoryNotFoundException extends DomainException {

    public CategoryNotFoundException(Long categoryId) {
        super("no category found with id " + categoryId);
    }
}
