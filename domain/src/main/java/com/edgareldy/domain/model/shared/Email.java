package com.edgareldy.domain.model.shared;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Immutable Value Object representing a validated email address.
 * <p>
 * Created by edgar.muhamyangabo on 7/11/26
 * Author : edgar.muhamyangabo
 * Date : 7/11/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public record Email(String value) {

    private static final Pattern FORMAT = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    public Email {
        Objects.requireNonNull(value, "value must not be null");
        if (!FORMAT.matcher(value).matches()) {
            throw new IllegalArgumentException("invalid email format: " + value);
        }
    }
}
