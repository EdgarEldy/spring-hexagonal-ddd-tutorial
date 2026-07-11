package com.edgareldy.domain.port.in;

import com.edgareldy.domain.model.customer.Customer;

import java.util.Optional;

/**
 * Inbound port: retrieve a single customer by id.
 * <p>
 * Created by edgar.muhamyangabo on 7/11/26
 * Author : edgar.muhamyangabo
 * Date : 7/11/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public interface GetCustomerUseCase {

    Optional<Customer> getCustomer(Long customerId);
}
