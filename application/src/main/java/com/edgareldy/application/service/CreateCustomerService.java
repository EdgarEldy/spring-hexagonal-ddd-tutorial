package com.edgareldy.application.service;

import com.edgareldy.domain.model.customer.Customer;
import com.edgareldy.domain.model.shared.Email;
import com.edgareldy.domain.port.in.CreateCustomerUseCase;
import com.edgareldy.domain.port.in.command.CreateCustomerCommand;
import com.edgareldy.domain.port.out.CustomerRepositoryPort;

import java.util.Objects;

/**
 * Implements {@link CreateCustomerUseCase}: builds the {@link Email} Value Object from the
 * command's raw string (format validation happens here, in the constructor it calls, not
 * before), then delegates the actual creation to {@link Customer#create}.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public final class CreateCustomerService implements CreateCustomerUseCase {

    private final CustomerRepositoryPort customerRepositoryPort;

    public CreateCustomerService(CustomerRepositoryPort customerRepositoryPort) {
        this.customerRepositoryPort = Objects.requireNonNull(customerRepositoryPort,
                "customerRepositoryPort must not be null");
    }

    @Override
    public Customer createCustomer(CreateCustomerCommand command) {
        Email email = new Email(command.email());
        Customer customer = Customer.create(command.firstName(), command.lastName(), command.telephone(), email,
                command.address());
        return customerRepositoryPort.save(customer);
    }
}
