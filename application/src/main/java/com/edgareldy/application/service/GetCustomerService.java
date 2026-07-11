package com.edgareldy.application.service;

import com.edgareldy.domain.model.customer.Customer;
import com.edgareldy.domain.port.in.GetCustomerUseCase;
import com.edgareldy.domain.port.out.CustomerRepositoryPort;

import java.util.Objects;
import java.util.Optional;

/**
 * Implements {@link GetCustomerUseCase}: pure orchestration, no business rule.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public final class GetCustomerService implements GetCustomerUseCase {

    private final CustomerRepositoryPort customerRepositoryPort;

    public GetCustomerService(CustomerRepositoryPort customerRepositoryPort) {
        this.customerRepositoryPort = Objects.requireNonNull(customerRepositoryPort,
                "customerRepositoryPort must not be null");
    }

    @Override
    public Optional<Customer> getCustomer(Long customerId) {
        return customerRepositoryPort.findById(customerId);
    }
}
