package com.edgareldy.domain.port.in.command;

import java.util.Objects;

/**
 * Input of {@code CreateCustomerUseCase}. {@code email} is a raw String here: the boundary
 * accepts client input as-is, and the {@code Email} Value Object validates its format when the
 * {@code Customer} entity is built from this command.
 * <p>
 * Created by edgar.muhamyangabo on 7/11/26
 * Author : edgar.muhamyangabo
 * Date : 7/11/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public record CreateCustomerCommand(String firstName, String lastName, String telephone, String email,
        String address) {

    public CreateCustomerCommand {
        Objects.requireNonNull(firstName, "firstName must not be null");
        Objects.requireNonNull(lastName, "lastName must not be null");
        Objects.requireNonNull(telephone, "telephone must not be null");
        Objects.requireNonNull(email, "email must not be null");
        Objects.requireNonNull(address, "address must not be null");
    }
}
